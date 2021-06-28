package com.example.mynews.slice;

import com.example.mynews.MainAbility;
import com.example.mynews.ResourceTable;
import com.example.mynews.model.NewsInfo;
import com.example.mynews.model.NewsType;
import com.example.mynews.provider.NewsListProvider;
import com.example.mynews.util.CommonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import ohos.agp.components.TabList;

import java.util.ArrayList;
import java.util.List;

public class MainAbilitySlice extends AbilitySlice {
    private TabList categoryTabList;
    private ListContainer newsListContainer;

    private List<NewsInfo> totalNewsDataList;
    private List<NewsInfo> newsDataList;

    private NewsListProvider newsListProvider;



    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        initView();
        initData();
        initListener();
    }

    private void initListener() {

        categoryTabList.addTabSelectedListener(new TabList.TabSelectedListener() {
            @Override
            public void onSelected(TabList.Tab tab) {
                newsDataList.clear();
                for (NewsInfo mTotalNewsData : totalNewsDataList) {
                    if (tab.getText().equals(mTotalNewsData.getType()) || tab.getPosition() == 0) {
                        newsDataList.add(mTotalNewsData);
                    }
                }
                updateListView();
            }

            @Override
            public void onUnselected(TabList.Tab tab) {

            }

            @Override
            public void onReselected(TabList.Tab tab) {

            }
        });

        newsListContainer.setItemClickedListener(
                (
                        ListContainer listContainer, Component
                        component,
                        int position, long l) ->

                {
                    Intent intent = new Intent();
                    Operation operation =
                            new Intent.OperationBuilder()
                                    .withBundleName(getBundleName())
                                    .withAbilityName(MainAbility.class.getName())
                                    .withAction("action.detail")
                                    .build();
                    intent.setOperation(operation);
                    intent.setParam(NewsDetailAbilitySlice.INTENT_TITLE, newsDataList.get(position).getTitle());
                    intent.setParam(NewsDetailAbilitySlice.INTENT_READ, newsDataList.get(position).getReads());
                    intent.setParam(NewsDetailAbilitySlice.INTENT_LIKE, newsDataList.get(position).getLikes());
                    intent.setParam(NewsDetailAbilitySlice.INTENT_CONTENT, newsDataList.get(position).getContent());
                    intent.setParam(NewsDetailAbilitySlice.INTENT_IMAGE, newsDataList.get(position).getImgUrl());
                    startAbility(intent);
                }
        );
    }

    private void updateListView() {
        newsListProvider.notifyDataChanged();
        newsListContainer.invalidate();
        newsListContainer.scrollToCenter(0);
    }

    private void initData() {
        Gson gson = new Gson();
        List<NewsType> newsTypeList =
                gson.fromJson(
                        CommonUtils.getStringFromJsonPath(this, "entry/resources/rawfile/news_type_datas.json"),
                        new TypeToken<List<NewsType>>() {
                        }.getType());
        totalNewsDataList =
                gson.fromJson(
                        CommonUtils.getStringFromJsonPath(this, "entry/resources/rawfile/news_datas.json"),
                        new TypeToken<List<NewsInfo>>() {
                        }.getType());

        newsDataList = new ArrayList<>();
        newsDataList.addAll(totalNewsDataList);

        newsListProvider = new NewsListProvider(newsDataList, this);
        newsListContainer.setItemProvider(newsListProvider);

        for (NewsType type : newsTypeList) {
            TabList.Tab tab = categoryTabList.new Tab(this);
            tab.setText(type.getName());
            categoryTabList.addTab(tab);
        }

    }

    private void initView() {
        categoryTabList = (TabList) this.findComponentById(ResourceTable.Id_tab_category_list);
        newsListContainer = (ListContainer) this.findComponentById(ResourceTable.Id_news_container);
    }
    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
