package com.example.mynews.slice;

import com.example.mynews.MainAbility;
import com.example.mynews.ResourceTable;
import com.example.mynews.model.NewsInfo;
import com.example.mynews.provider.DevicesListProvider;
import com.example.mynews.util.CommonUtils;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.agp.window.dialog.CommonDialog;
import ohos.distributedschedule.interwork.DeviceInfo;
import ohos.distributedschedule.interwork.DeviceManager;

import java.util.ArrayList;
import java.util.List;

public class NewsDetailAbilitySlice extends AbilitySlice {
    public static final String INTENT_TITLE = "intent_title";
    public static final String INTENT_READ = "intent_read";
    public static final String INTENT_LIKE = "intent_like";
    public static final String INTENT_CONTENT = "intent_content";
    public static final String INTENT_IMAGE = "intent_image";

    private DependentLayout parentLayout;
    private TextField commentFocus;
    private Image iconShared;
    private NewsInfo curNewsInfo;

    private List<DeviceInfo> devices = new ArrayList<>();

    private CommonDialog dialog;
    private static final int DIALOG_SIZE_WIDTH = 900;
    private static final int DIALOG_SIZE_HEIGHT = 800;
    private static final int WAIT_TIME = 30000;


    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_news_detail_layout);
        curNewsInfo = getCurNewsInfo(intent);
        initView();
        initListener();
    }

    public void initListener() {

        iconShared.setClickedListener(
                v -> {
                    initDevices();
                    showDeviceList();
                });
    }

    private void startAbilityFA(String devicesId) {
        Intent intent = new Intent();
        Operation operation =
                new Intent.OperationBuilder()
                        .withDeviceId(devicesId)
                        .withBundleName(getBundleName())
                        .withAbilityName(MainAbility.class.getName())
                        .withFlags(Intent.FLAG_ABILITYSLICE_MULTI_DEVICE)
                        .withAction("action.detail")
                        .build();
        intent.setOperation(operation);
        intent.setParam(NewsDetailAbilitySlice.INTENT_TITLE, curNewsInfo.getTitle());
        intent.setParam(NewsDetailAbilitySlice.INTENT_READ, curNewsInfo.getReads());
        intent.setParam(NewsDetailAbilitySlice.INTENT_LIKE, curNewsInfo.getLikes());
        intent.setParam(NewsDetailAbilitySlice.INTENT_CONTENT, curNewsInfo.getContent());
        intent.setParam(NewsDetailAbilitySlice.INTENT_IMAGE, curNewsInfo.getImgUrl());
        startAbility(intent);
    }

    private void showDeviceList() {
        dialog = new CommonDialog(NewsDetailAbilitySlice.this);
        dialog.setAutoClosable(true);
        dialog.setTitleText("Harmony devices");
        dialog.setSize(DIALOG_SIZE_WIDTH, DIALOG_SIZE_HEIGHT);
        ListContainer devicesListContainer = new ListContainer(getContext());
        DevicesListProvider devicesListProvider = new DevicesListProvider(devices, this);
        devicesListContainer.setItemProvider(devicesListProvider);
        devicesListContainer.setItemClickedListener(
                (listContainer, component, position, id) -> {
                    dialog.destroy();
                    startAbilityFA(devices.get(position).getDeviceId());
                });
        devicesListProvider.notifyDataChanged();
        dialog.setContentCustomComponent(devicesListContainer);
        dialog.show();
    }

    private void initDevices() {
        if (devices.size() > 0) {
            devices.clear();
        }
        List<DeviceInfo> deviceInfos =
                DeviceManager.getDeviceList(DeviceInfo.FLAG_GET_ONLINE_DEVICE);
        devices.addAll(deviceInfos);
    }

    private NewsInfo getCurNewsInfo(Intent intent) {
        curNewsInfo = new NewsInfo();
        curNewsInfo.setLikes(intent.getStringParam(INTENT_LIKE));
        curNewsInfo.setReads(intent.getStringParam(INTENT_READ));
        curNewsInfo.setContent(intent.getStringParam(INTENT_CONTENT));
        curNewsInfo.setTitle(intent.getStringParam(INTENT_TITLE));
        curNewsInfo.setImgUrl(intent.getStringParam(INTENT_IMAGE));
        return curNewsInfo;
    }

    public void initView() {
        parentLayout = (DependentLayout) findComponentById(ResourceTable.Id_parent_layout);
        commentFocus = (TextField) findComponentById(ResourceTable.Id_text_file);
        iconShared = (Image) findComponentById(ResourceTable.Id_button4);
        Text newsRead = (Text) findComponentById(ResourceTable.Id_read_num);
        Text newsLike = (Text) findComponentById(ResourceTable.Id_like_num);
        Text newsTitle = (Text) findComponentById(ResourceTable.Id_title_text);
        Text newsContent = (Text) findComponentById(ResourceTable.Id_title_content);
        Image newsImage = (Image) findComponentById(ResourceTable.Id_image_content);
        newsRead.setText("reads: " + curNewsInfo.getReads());
        newsLike.setText("likes: " + curNewsInfo.getLikes());
        newsTitle.setText("Original title: " + curNewsInfo.getTitle());
        newsContent.setText(curNewsInfo.getContent());
        newsImage.setPixelMap(CommonUtils.getPixelMapFromPath(this, curNewsInfo.getImgUrl()));
    }
}