package com.amit.cambium.models.repository;

import android.util.Log;

import com.amit.cambium.models.Project;
import com.amit.cambium.utils.VolleySingleton;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 *
 * A local data storage implemented using Realm mobile database
 *
 * We do not require any pagination with realm
 * as realm db only fetches an object when it is being accessed via a get method.
 *
 * Created by Amit Barjatya on 10/29/17.
 */

public class DataSourceImpl implements DataSource {

    private static final String TAG = "DataSourceImpl";

    /**
     * Realm instance held by this implementation
     */
    Realm realm;

    /**
     * Data listener callback used to notify the presenters
     */
    DataListener mDataListener;

    /**
     * Error listener callback used to notify the presenter for any error
     */
    ErrorListener mErrorListener;

    /**
     * a reference to projects in realm
     */
    RealmChangeListener<RealmResults<Project>> projectsListener;

    /**
     * list of projects currently in realm
     */
    RealmResults<Project> projects;

    /**
     * Initialze a realm instance
     */
    public DataSourceImpl() {
        realm = Realm.getDefaultInstance();
    }

    /**
     * Provides the list of all projects currently in database
     * if there are no projects, asks the server for the projects
     * and notifies the presenter for any changes
     *
     * @param dataListener listener for data
     * @param errorListener listener for any error
     * @return  list of projects
     */
    @Override
    public List<Project> getProjectListAsync(DataListener dataListener, ErrorListener errorListener) {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        this.mDataListener = dataListener;
        this.mErrorListener = errorListener;
        if (mDataListener == null) {
            Log.e(TAG, "no listener attached for receiving data");
            return null;
        } else {
            fetchFromServerIfNoCache();
            projects = realm.where(Project.class).findAllAsync();
            projectsListener = new RealmChangeListener<RealmResults<Project>>() {
                @Override
                public void onChange(RealmResults<Project> projects) {
                    mDataListener.onData(projects);
                }
            };
            projects.addChangeListener(projectsListener);
            return projects;
        }
    }

    /**
     * Provides a Project with the given serial number
     *
     * @param serialNumber the serial number of the Project
     * @return
     */
    @Override
    public Project getProjectBySerialNumber(long serialNumber) {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();
        return realm.where(Project.class)
                .equalTo("serialNumber", serialNumber)
                .findFirst();
    }

    /**
     * If there are no projects in local database, ask the server for projects
     */
    private void fetchFromServerIfNoCache() {
        long count = realm.where(Project.class).count();
        if (count == 0) {
            fetchFromServer();
        }
    }

    /**
     * Apply backer filter on the list of projects
     *
     * @param minValue minimum number of backers
     * @param maxValue maximum number of backers
     * @return list of filtered projects
     */
    @Override
    public List<Project> withBackerFilter(long minValue, long maxValue) {
        if (realm.isClosed())
            realm = Realm.getDefaultInstance();

        return realm.where(Project.class)
                .greaterThan("numBackers",minValue)
                .lessThan("numBackers",maxValue)
                .findAll();
    }

    /**
     * Sort the Project list with ascending end time
     * @return list of sorted projects
     */
    @Override
    public List<Project> withEndAscendingSort() {
        return realm.where(Project.class)
                .findAllSorted("endTime", Sort.ASCENDING);
    }

    /**
     * Sort the Project list with descending end time
     * @return list of sorted projects
     */
    @Override
    public List<Project> withEndDescendingSort() {
        return realm.where(Project.class)
                .findAllSorted("endTime", Sort.DESCENDING);
    }

    /**
     * Sort the Project list with title a to z
     * @return list of sorted projects
     */
    @Override
    public List<Project> withA2ZSort() {
        return realm.where(Project.class)
                .findAllSorted("title", Sort.ASCENDING);
    }

    /**
     * Sort the Project list with title z to a
     * @return list of sorted projects
     */
    @Override
    public List<Project> withZ2ASort() {
        return realm.where(Project.class)
                .findAllSorted("title", Sort.DESCENDING);

    }

    /**
     * Fetch projects from server
     * On Success ==> save it to local database
     * on error ==> notify the caller
     * Uses a volley instance
     */
    private void fetchFromServer() {
        String url = "http://starlord.hackerearth.com/kickstarter";
        JsonArrayRequest arrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                saveResponseToStorage(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                percolateError(error);
            }
        });
        arrayRequest.setRetryPolicy(new DefaultRetryPolicy());
        arrayRequest.setShouldCache(true);
        VolleySingleton.getInstance().getRequestQueue().add(arrayRequest);

    }

    /**
     * Notify the data consumer for any error
     * @param error
     */
    private void percolateError(VolleyError error) {
        if (mErrorListener!=null){
            mErrorListener.onError(error);
        }
    }

    /**
     * Saves a list of JSONArray of projects to local database
     * @param array the json representation of list of projects
     */
    private void saveResponseToStorage(final JSONArray array) {
        if (realm.isClosed())
            return;
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int i = 0; i < array.length(); i++) {
                    try {
                        Project project = Project.fromJSONObject(array.getJSONObject(i));
                        if (project != null) {
                            realm.copyToRealmOrUpdate(project);
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, e.toString());
                    }
                }
            }
        });
    }

    @Override
    public List<Project> withTitleFilter(String s) {
        if (s==null || s.isEmpty()){
            return projects;
        }else{
            return realm.where(Project.class)
                    .contains("title",s, Case.INSENSITIVE)
                    .findAll();
        }
    }

    /**
     * Clear all the resources used by this instance
     */
    @Override
    public void clearListeners() {
        if (projects != null && projectsListener != null) {
            projects.removeChangeListener(projectsListener);
        }
        if (mDataListener != null) {
            mDataListener = null;
        }
        if (mErrorListener != null) {
            mErrorListener = null;
        }

        realm.close();
    }


    //getters and setters ...

    public DataListener getDataListener() {
        return mDataListener;
    }

    public void setDataListener(DataListener mDataListener) {
        this.mDataListener = mDataListener;
    }

    public ErrorListener getErrorListener() {
        return mErrorListener;
    }

    public void setErrorListener(ErrorListener mErrorListener) {
        this.mErrorListener = mErrorListener;
    }
}
