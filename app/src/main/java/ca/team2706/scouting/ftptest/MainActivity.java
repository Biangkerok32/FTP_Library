package ca.team2706.scouting.ftptest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.InputStream;
import java.net.ConnectException;
import java.security.Permission;

public class MainActivity extends AppCompatActivity implements FTPRequester {

    //FTPClient object
    FTPClient ftpclient = new FTPClient("ftp.team2706.ca", "scout", "2706IsWatching!", Environment.getExternalStorageDirectory() + "/frc2706/files");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Android code
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Request for permissions
        requestPermissions();
        try {
            //Try to connect
            ftpclient.connect();
            if (ftpclient.isConnected()) {
                //get a directory listing to enter PASV mode
                ftpclient.dir(this);
                //Sync files between device and server
                ftpclient.syncAllFiles(this);
            } else {
                //FTPClient didn't connect.
                Log.d("FTPClient", "Failed to connect, cannot DIR.");
            }
        } catch (ConnectException e) {
            // it must have failed to connect, or something threw an error so do nothing.
        }
    }

    //Start of callback functions from FTPRequester.java
    public void uploadFileCallback(String localFilename, String remoteFilename){
        //place code here for when the client is done uploading a file. This is run on the background thread
        //Arg localFilename: the local path of the uploaded file.
        //Arg remoteFilename: the remote path of the uploaded file.
    }
    public void downloadFileCallback(String localFilename, String remoteFilename) {
        //Place code here for when the client is done downloading a file. This is run on the background thread
        //Arg localFilename: the local path of the downloaded file.
        //Arg remoteFilename: the remote path of the downloaded file.
    }
    public void syncCallback(int changedFiles){
        //Place code here for when the client is done syncing files. This is run on the background thread
        //Arg changedFiles: the number of files uploaded / downloaded.
    }
    public void dirCallback(FTPFile[] filelisting){
        //Place code here for when the client is done getting a file listing. This is run on the background thread
        //Arg filelisting: the FTPFile array of the directorys and files from the listing
    }
    //End of callback functions from FTPRequester.java

    //Method that checks for and requests for permissions
    void requestPermissions(){
        Boolean ReadStoragePerm = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
        Boolean WriteStoragePerm = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
        if (ReadStoragePerm&&WriteStoragePerm) {
            //We already have both permissions, so we're good.
        }else{
            //We need to get one of the two permissions, so we'll ask for both.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        }
    }
}
