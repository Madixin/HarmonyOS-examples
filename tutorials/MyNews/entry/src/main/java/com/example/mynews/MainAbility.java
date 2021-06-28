package com.example.mynews;

import com.example.mynews.slice.MainAbilitySlice;
import com.example.mynews.slice.NewsDetailAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
        addActionRoute("action.detail", NewsDetailAbilitySlice.class.getName());
        requestPermissionsFromUser(new String[]{"ohos.permission.DISTRIBUTED_DATASYNC"}, 0);
    }
}
