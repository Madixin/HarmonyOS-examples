package com.example.mynews.provider;

import com.example.mynews.ResourceTable;
import com.example.mynews.model.NewsInfo;
import com.example.mynews.util.CommonUtils;
import ohos.agp.components.*;
import ohos.app.Context;

import java.util.List;

public class NewsListProvider extends BaseItemProvider {
    private List<NewsInfo> newsInfoList;
    private Context context;

    /**
     * constructor function
     *
     * @param newsInfoList list info
     * @param context context
     * @since 2020-12-04
     */
    public NewsListProvider(List<NewsInfo> newsInfoList, Context context) {
        this.newsInfoList = newsInfoList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return newsInfoList == null ? 0 : newsInfoList.size();
    }

    @Override
    public Object getItem(int i) {
        return newsInfoList.get(i);
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
            temp = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_item_news_layout, null, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (Text) temp.findComponentById(ResourceTable.Id_item_news_title);
            viewHolder.image = (Image) temp.findComponentById(ResourceTable.Id_item_news_image);
            temp.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) temp.getTag();
        }
        viewHolder.title.setText(newsInfoList.get(position).getTitle());
        viewHolder.image.setPixelMap(CommonUtils.getPixelMapFromPath(context, newsInfoList.get(position).getImgUrl()));
        return temp;
    }

    /**
     * ViewHolder which has title and image
     *
     * @since 2020-12-04
     */
    private static class ViewHolder {
        Text title;
        Image image;
    }
}