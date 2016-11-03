package com.languantan.gotcha.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.*;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        if (!getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
                    .show();
        } else {
            String cameraId = findFrontFacingCamera();
            if (cameraId.equals("-1")) {
                Toast.makeText(this, "No front facing camera found.",
                        Toast.LENGTH_LONG).show();
            } else {
                try {
                    manager.openCamera(cameraId, new CameraDevice.StateCallback() {
                        @Override
                        public void onOpened(CameraDevice camera) {
                            try {
                                CaptureRequest.Builder request = camera.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);

                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onDisconnected(CameraDevice camera) {

                        }

                        @Override
                        public void onError(CameraDevice camera, int error) {

                        }
                    }, null);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void submitName(View view) {
        String name = ((EditText) findViewById(R.id.input_name)).getText().toString();
        try {
            sendName(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new AlertDialog.Builder(this).setTitle("Thank you")
                .setMessage("Thanks " + name + "! Please return the phone after use.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    private void sendName(String name) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://maker.ifttt.com/trigger/DeviceWallUse/with/key/" + BuildConfig.SECRET_KEY;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url,
                new JSONObject("{ \"value1\": \"" + name + "\"}"),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                });

        queue.add(jsonObjectRequest);
    }


    private String findFrontFacingCamera() {
        String cameraId = "-1";
        // Search for the front facing camera
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            for (String id : manager.getCameraIdList()) {
                CameraCharacteristics characters = manager.getCameraCharacteristics(id);
                if (characters.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                    Log.d("MainActivity", "Camera found");
                    cameraId = id;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cameraId;
    }
}