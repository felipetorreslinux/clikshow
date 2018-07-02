package com.clikshow.Fragmentos;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.clikshow.API.APIServer;
import com.clikshow.Fragmentos.Models.Feed_Eventos;
import com.clikshow.Fragmentos.Models.FeedSlide;
import com.clikshow.R;
import com.clikshow.Service.Service_Eventos;
import com.tmall.ultraviewpager.UltraViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class Feed_Lista_Fragment extends Fragment {

    public static int viewPageSlide = 0;

    LinearLayout box_feed_lista;

    private UltraViewPager mViewPager;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Feed_Eventos> eventos = new ArrayList<>();
    private List<FeedSlide> slides = new ArrayList<>();
    private SwipeRefreshLayout pull_refresh;

    private LinearLayout box_feed;
    private ViewStub view_not_conexao;
    private ViewStub loading_tab_feed;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_lista_feed, container, false);

        box_feed_lista = (LinearLayout) rootView.findViewById(R.id.box_feed_lista);

        loading_tab_feed = (ViewStub) rootView.findViewById(R.id.loading_tab_feed);
        pull_refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.pull_refresh_feed_eventos);
        box_feed = (LinearLayout) rootView.findViewById(R.id.box_feed);
        view_not_conexao = (ViewStub) rootView.findViewById(R.id.view_not_conexao);

        if(APIServer.conexao(getActivity()) == true){
            loading_tab_feed.inflate();
            mRecyclerView = rootView.findViewById(R.id.alleventsOne);
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setNestedScrollingEnabled(false);
            mViewPager = rootView.findViewById(R.id.slide_images_destaques_feed);

            Service_Eventos.lista_eventos(getActivity(), slides, eventos, mRecyclerView, mViewPager, pull_refresh, loading_tab_feed);

            animation();

            // PullToRefresh
            pull_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Service_Eventos.lista_eventos(getActivity(), slides, eventos, mRecyclerView, mViewPager, pull_refresh, loading_tab_feed);
                }
            });
        }else{
            view_not_conexao.inflate();
            eventos.clear();
            slides.clear();
        }
        return  rootView;
    };

    private void animation(){
        Animation animation = new TranslateAnimation(0, 0,2000, 0);
        animation.setDuration(600);
        animation.setFillAfter(true);
        box_feed_lista.startAnimation(animation);
    };

    private void SlideAuto(){
        final Handler handler = new Handler();
        final Runnable Upadate = new Runnable() {
            @Override
            public void run() {
                if(viewPageSlide == 5) {
                    viewPageSlide = 0;
                    mViewPager.setCurrentItem(viewPageSlide, true);
                }
                mViewPager.setCurrentItem(viewPageSlide++, true);
            }
        };

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Upadate);
            }
        }, 1000, 3000);
    };

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }
}
