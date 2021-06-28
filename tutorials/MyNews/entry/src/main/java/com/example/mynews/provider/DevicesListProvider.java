package com.example.mynews.provider;

import com.example.mynews.ResourceTable;
import ohos.agp.components.*;
import ohos.app.Context;
import ohos.distributedschedule.interwork.DeviceInfo;

import java.util.List;

public class DevicesListProvider extends BaseItemProvider {
    private List<DeviceInfo> deviceInfoList;
    private Context context;

    /**
     * constructor function
     *
     * @param listBasicInfo list info
     * @param context context
     * @since 2020-12-04
     */
    public DevicesListProvider(List<DeviceInfo> listBasicInfo, Context context) {
        this.deviceInfoList = listBasicInfo;
        this.context = context;
    }

    @Override
    public int getCount() {
        return deviceInfoList == null ? 0 : deviceInfoList.size();
    }

    @Override
    public Object getItem(int i) {
        return deviceInfoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Component getComponent(int position, Component component, ComponentContainer componentContainer) {
        ViewHolder viewHolder;
        Component temp = component;
        if (temp == null) {
            temp = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_device_list_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.devicesName = (Text) temp.findComponentById(ResourceTable.Id_item_chlid_textview);
            temp.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) temp.getTag();
        }
        viewHolder.devicesName.setText(deviceInfoList.get(position).getDeviceName());
        return temp;
    }

    /**
     * ViewHolder which has devicesName
     *
     * @since 2020-12-04
     */
    private static class ViewHolder {
        private Text devicesName;
    }
}