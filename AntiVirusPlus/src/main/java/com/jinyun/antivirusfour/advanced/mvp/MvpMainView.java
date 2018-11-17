package com.jinyun.antivirusfour.advanced.mvp;

public interface MvpMainView extends MvpLoadingView{
   void showToast(String msg);
   void updateView();
}
