package com.champhay.Model.custom.component;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.champhay.Model.handler.social.FacebookAPI;
import com.champhay.Model.handler.social.FacebookContent;
import com.champhay.Model.other.Show;
import com.champhay.mcomics.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * Created by HoangTP
 */

public class NavigationDrawer implements View.OnClickListener {
    private LayoutInflater inflater;
    private ViewGroup parent;
    private View mainView;
    private Activity activity;
    private Dialog brightnessDialog, viewModeDialog;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private LinearLayout btn_vertical, btn_horizontal;
    private SettingHandle settingHandle;
    private SeekBar seekBar;
    private long firstClickTime = -1, secondClickTime = -1;
    private Dialog dialog;

    private final int REQUEST_CODE = 200;
    private TableRow btn_brightness, btn_viewMode, btn_exit;
    private final float ONE_PERCENT = 255f / 100f;

    public NavigationDrawer(final Activity activity, int layout, ViewGroup viewGroup) {
        this.activity = activity;
        this.inflater = (LayoutInflater.from(activity));
        this.parent = viewGroup;
        mappings(layout);

        ((LinearLayout) mainView).addView(toolbar, 0);
        ((FrameLayout) parent.findViewById(R.id.root)).addView(mainView);
        dialog = new Dialog(activity);
        showDialog(R.id.btn_info, R.layout.view_information_app);
        setButtonOnClick();
        createBrightnessDialog();
        createViewModeDialog();
        doubleClickExitButton();

        createNavigationButton();
    }

    private void mappings(int layout) {
        toolbar = inflater.inflate(R.layout.view_toolbar, ((ViewGroup) mainView), false).findViewById(R.id.toolBar);
        drawerLayout = ((DrawerLayout) parent);
        mainView = (inflater.inflate(layout, parent, false));
        btn_brightness = parent.findViewById(R.id.btn_brightness);
        btn_viewMode = parent.findViewById(R.id.btn_viewMode);
        settingHandle = new SettingHandle(activity);
    }

    private void showDialog(int id, final int layout) {
        ImageView thongTin = parent.findViewById(id);
        thongTin.setOnClickListener(view -> {
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            dialog.setContentView(layout);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.show();
        });

    }

    private void doubleClickExitButton() {
        ImageView btn_Exit = parent.findViewById(R.id.btn_exit);
        btn_Exit.setOnClickListener(v -> {
            if (firstClickTime == -1) {
                Show.toastSHORT(activity, "Nhấn 2 lần để thoát");
                firstClickTime = System.currentTimeMillis();
            } else if ((firstClickTime != -1) && ((secondClickTime = System.currentTimeMillis()) - firstClickTime) <= 2000) {
                System.exit(0);
            } else if ((firstClickTime != -1) && ((secondClickTime = System.currentTimeMillis()) - firstClickTime) > 2000) {
                Show.toastSHORT(activity, "Nhấn 2 lần để thoát");
                firstClickTime = System.currentTimeMillis();
            }
        });
    }

    private void setButtonOnClick() {
        btn_brightness.setOnClickListener(this);
        btn_viewMode.setOnClickListener(this);
    }

    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
                if (Settings.System.canWrite(activity)) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_SETTINGS}, REQUEST_CODE);
                } else {
                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.setData(Uri.parse("package:" + activity.getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }
            }
        }
    }

    private int getScreenWidth() {
        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(point);
        return point.x;
    }

    public int getScreenHeight() {
        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(point);
        return point.y;
    }

    private void createBrightnessDialog() {
        getPermission();

        brightnessDialog = new Dialog(activity);
        brightnessDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        brightnessDialog.setContentView(R.layout.view_dialog_brightness);

        brightnessDialog.findViewById(R.id.dialog_brightness).getLayoutParams().width = (int) (getScreenWidth() * 0.90);

        seekBar = brightnessDialog.findViewById(R.id.sbBrightness);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, progress);
                ((TextView) brightnessDialog.findViewById(R.id.txv_currentBrightness)).setText((int) (progress / ONE_PERCENT) + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void createViewModeDialog() {
        viewModeDialog = new Dialog(activity);
        viewModeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewModeDialog.setContentView(R.layout.view_dialog_swipe_mode);
        viewModeDialog.findViewById(R.id.dialog_viewMode).getLayoutParams().width = (int) (getScreenWidth() * 0.90);

        btn_vertical = viewModeDialog.findViewById(R.id.ll_btn_Vertical);
        btn_horizontal = viewModeDialog.findViewById(R.id.ll_btn_Horizontal);
        refreshViewMode();

        btn_vertical.setOnClickListener(v -> {
            Show.log("btn_vertical", LinearLayoutManager.VERTICAL + "");
            settingHandle.setOrientation(LinearLayoutManager.VERTICAL);
            refreshViewMode();
        });

        btn_horizontal.setOnClickListener(v -> {
            Show.log("btn_horizontal", LinearLayoutManager.HORIZONTAL + "");
            settingHandle.setOrientation(LinearLayoutManager.HORIZONTAL);
            refreshViewMode();
        });
    }

    private void refreshViewMode() {
        clearBorderViewMode();
        if (settingHandle.getOrientation() == LinearLayoutManager.VERTICAL) {
            btn_vertical.setBackgroundColor(Color.parseColor("#000000"));
        } else {
            btn_horizontal.setBackgroundColor(Color.parseColor("#000000"));
        }
        Show.log("orientation", settingHandle.getOrientation() + "");
    }

    private void clearBorderViewMode() {
        btn_vertical.setBackgroundColor(Color.parseColor("#FFFFFF"));
        btn_horizontal.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }

    private void createNavigationButton() {
        toolbar.findViewById(R.id.navigation_button).setOnClickListener(v -> showHideNavigation());
    }

    private void showHideNavigation() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == btn_brightness.getId()) {
            brightnessClicked();
        } else if (id == btn_viewMode.getId()) {
            viewModeClicked();
        }
    }

    private void likesClicked() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/MComics-252252888524492/"));
        activity.startActivity(browserIntent);
    }

    private void brightnessClicked() {
        int currentBrightness = Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, -1);
        seekBar.setProgress(currentBrightness);
        brightnessDialog.show();
    }

    private void viewModeClicked() {
        viewModeDialog.show();
    }

    public void hideActionbar() {
        toolbar.setVisibility(View.GONE);
    }

    public SettingHandle getSettingHandle() {
        return settingHandle;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
