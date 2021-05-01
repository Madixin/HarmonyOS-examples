package com.harmonyos.thread.slice;

import com.harmonyos.thread.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.app.dispatcher.task.Revocable;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class MainAbilitySlice extends AbilitySlice {

    // 定义日志标签
    private static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0x00201, "MainAbilitySlice");
    // 定义任务ID
    private static final int EVENT_MESSAGE_NORMAL = 666;

    private Button eventSleepBtn;
    private Button dispatcherSleepBtn;
    private Text resultText;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        HiLog.info(LABEL, "onStart：" + +Thread.currentThread().getId());
        initView();
    }

    private void initView() {
        if (findComponentById(ResourceTable.Id_text_helloworld) instanceof Text) {
            resultText = (Text) findComponentById(ResourceTable.Id_text_helloworld);
        }
        if (findComponentById(ResourceTable.Id_btn_handler_sleep) instanceof Button) {
            eventSleepBtn = (Button) findComponentById(ResourceTable.Id_btn_handler_sleep);
            eventSleepBtn.setClickedListener(this::takeHandlerSleep);
        }
        if (findComponentById(ResourceTable.Id_btn_dispatcher_sleep) instanceof Button) {
            dispatcherSleepBtn = (Button) findComponentById(ResourceTable.Id_btn_dispatcher_sleep);
            dispatcherSleepBtn.setClickedListener(this::takeDispatcherSleep);
        }
    }


    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    private void takeDispatcherSleep(Component component) {
        HiLog.info(LABEL, "Hey! You have take a dispatcher sleep:" + Thread.currentThread().getId());
        TaskDispatcher globalTaskDispatcher = getGlobalTaskDispatcher(TaskPriority.DEFAULT);
        Revocable revocable = globalTaskDispatcher.asyncDispatch(new Runnable() {
            @Override
            public void run() {
                HiLog.info(LABEL, "start dispatcher task run：" + +Thread.currentThread().getId());
                try {
                    Thread.sleep(5000l);//等待5秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                HiLog.info(LABEL, "end dispatcher task run：" + +Thread.currentThread().getId());

                getUITaskDispatcher().asyncDispatch(
                        () -> {
                            HiLog.info(LABEL, "refresh ui ：" + +Thread.currentThread().getId());
                            // UI线程更新界面
                            resultText.setText("DispatcherSleep finished:" + Thread.currentThread().getId());
                        }
                );
            }
        });
        HiLog.info(LABEL, "end takeDispatcherSleep:" + Thread.currentThread().getId());
    }

    private void takeHandlerSleep(Component component) {
        HiLog.info(LABEL, "Hey! You have take a handler sleep:" + Thread.currentThread().getId());

        // 创建和使用非托管的EventRunner
        EventRunner runner = EventRunner.create("handler sleep thread");
        MyEventHandler myHandler = new MyEventHandler(runner);

        long param = 0L;
        InnerEvent normalInnerEvent = InnerEvent.get(EVENT_MESSAGE_NORMAL, param, EventRunner.current());
        // 发送一个事件到事件队列，延时为0ms, 优先级为LOW
        myHandler.sendEvent(normalInnerEvent, 0, EventHandler.Priority.IMMEDIATE);
    }

    private class MyEventHandler extends EventHandler {
        private MyEventHandler(EventRunner runner) {
            super(runner);
        }

        // 重写实现processEvent方法
        @Override
        public void processEvent(InnerEvent event) {
            super.processEvent(event);
            if (event == null) {
                return;
            }
            HiLog.info(LABEL, "start processEvent::" + Thread.currentThread().getId());
            int eventId = event.eventId;
            switch (eventId) {
                case EVENT_MESSAGE_NORMAL:
                    try {
                        Thread.sleep(5000l);//等待5秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Object object = event.object;
                    if (object instanceof EventRunner) {
                        // 将原先线程的EventRunner实例投递给新创建的线程
                        EventRunner uiEventRunner = (EventRunner) object;
                        // 将原先线程的EventRunner实例与新创建的线程的EventHandler绑定
                        EventHandler uiEventHandler = new EventHandler(uiEventRunner) {
                            @Override
                            public void processEvent(InnerEvent event) {
                                // 在原UI线程更新界面
                                resultText.setText("HandlerSleep finished:" + Thread.currentThread().getId());
                            }
                        };
                        InnerEvent uiEvent = InnerEvent.get(1, 0L, null);
                        uiEventHandler.sendEvent(uiEvent); // 投递事件回原先UI的线程
                    }
                    break;
                default:
                    break;
            }
            HiLog.info(LABEL, "finish processEvent::" + Thread.currentThread().getId());
        }
    }
}