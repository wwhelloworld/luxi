package com.lcsd.luxi.ui.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gongwen.marqueen.SimpleMF;
import com.gongwen.marqueen.SimpleMarqueeView;
import com.lcsd.luxi.R;
import com.lcsd.luxi.entity.Frag01_list;
import com.lcsd.luxi.entity.Frag01_news;
import com.lcsd.luxi.refresh.OnItemClickListener;
import com.lcsd.luxi.refresh.RollPagerView;
import com.lcsd.luxi.ui.activity.AboutUsActivity;
import com.lcsd.luxi.ui.activity.DianBoListActivity;
import com.lcsd.luxi.ui.activity.HotNoticeActivity;
import com.lcsd.luxi.ui.activity.MainActivity;
import com.lcsd.luxi.ui.activity.NewsDetialActivity;
import com.lcsd.luxi.ui.activity.VideoPlayerActivity;
import com.lcsd.luxi.ui.activity.XWZXActivity;
import com.lcsd.luxi.ui.activity.ZTZLActivity;
import com.lcsd.luxi.ui.activity.ZWActivity;
import com.lcsd.luxi.ui.fragment.Fragment2;
import com.lcsd.luxi.ui.fragment.Fragment3;
import com.lcsd.luxi.utils.DateUtils;
import com.lcsd.luxi.utils.GlideUtils;
import com.lcsd.luxi.view.CustomVRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Frag01_recycle_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LinearLayout linearLayout;
    //轮播图
    public static final int ITEM_01 = 0;
    //顶部滚动消息
    public static final int ITEM_02 = 1;
    //顶部途径两条消息
    public static final int ITEM_03 = 2;
    //栏目列表
    public static final int ITEM_04 = 3;
    //专题专栏
    public static final int ITEM_05 = 4; //普通布局
    //普通新闻消息
    public static final int ITEM_06 = 5; //普通布局


    private List<Frag01_list.THeadSlideNews> bannerlist;
    private List<Frag01_list.TTopNewslist> toplist;
    private List<Frag01_list.TVideoList> recommendlist;
    private List<Frag01_list.TNewxmlbList> lanmulist;
    private List<Frag01_list.TColumnClick> videolist;
    private List<Frag01_news.TContent.TRslist> newslist;
    //


    public Frag01_recycle_adapter(Context mContext, List<Frag01_list.THeadSlideNews> bannerlist,
                                  List<Frag01_list.TTopNewslist> toplist,
                                  List<Frag01_list.TVideoList> recommendlist,
                                  List<Frag01_list.TNewxmlbList> lanmulist,
                                  List<Frag01_list.TColumnClick> videolist,
                                  List<Frag01_news.TContent.TRslist> newslist
    ) {
        this.mContext = mContext;

        this.bannerlist = bannerlist;
        this.toplist = toplist;
        this.recommendlist = recommendlist;
        this.lanmulist = lanmulist;
        this.videolist = videolist;
        this.newslist = newslist;
    }

    /**
     * 创建缓存
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == ITEM_01) {//轮播图
            view = getView(R.layout.item_recycle_bannner);
            BannerHolder bannerHolder = new BannerHolder(view);
            return bannerHolder;

        } else if (viewType == ITEM_02) {
            view = getView(R.layout.topnews_recycleview);
            TopNewsHolder topNewsHolder = new TopNewsHolder(view);
            return topNewsHolder;
        } else if (viewType == ITEM_03) {
            view = getView(R.layout.recommend_recycleview);
            RecommendHolder recommendHolder = new RecommendHolder(view);
            return recommendHolder;

        } else if (viewType == ITEM_04) {//栏目
            view = getView(R.layout.lanmu_recycleview);
            LanmuHolder lanmuHolder = new LanmuHolder(view);
            return lanmuHolder;

        } else if (viewType == ITEM_05) {//专题专栏
            view = getView(R.layout.dslm_recycleview);
            DslmHolder dslmHolder = new DslmHolder(view);
            return dslmHolder;

        } else {//新闻列表
            view = getView(R.layout.item_newslist_recycleview);
            NewsListHolder newsListHolder = new NewsListHolder(view);
            return newsListHolder;
        }
    }

    /**
     * 上面用来引入布局的方法
     */
    private View getView(int view) {
        View view1 = View.inflate(mContext, view, null);
        return view1;
    }

    /**
     * 给缓存控件设置数据
     */
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        //判断不同的ViewHolder做不同的处理
        if (holder instanceof BannerHolder) {//轮播图
            BannerHolder bannerHolder = (BannerHolder) holder;
            //调用设置轮播图相关方法

            Banner_adapter banner_adapter = new Banner_adapter(mContext, bannerlist, bannerHolder.rollPagerView);
            bannerHolder.rollPagerView.setparentTouch((ViewGroup) holder.itemView.getRootView());//引入根布局，解决滑动冲突
            bannerHolder.rollPagerView.setAdapter(banner_adapter);
            bannerHolder.rollPagerView.setAnimationDurtion(2000);
            bannerHolder.rollPagerView.setHintPadding(0, 0, 0, 20);
            bannerHolder.rollPagerView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    mContext.startActivity(new Intent(mContext, NewsDetialActivity.class)
                            .putExtra("url", bannerlist.get(position).getUrl())
                            .putExtra("title", bannerlist.get(position).getTitle()));

                }
            });
        } else if (holder instanceof TopNewsHolder) {
            TopNewsHolder topNewsHolder = (TopNewsHolder) holder;
            List<String> toplisttitle = new ArrayList<>();
            for (Frag01_list.TTopNewslist str : toplist) {
                toplisttitle.add(str.getTitle());

            }
            SimpleMF<String> marqueeFactory = new SimpleMF(mContext);
            marqueeFactory.setData(toplisttitle);
            topNewsHolder.marqueeView.setMarqueeFactory(marqueeFactory);
            topNewsHolder.marqueeView.startFlipping();
            topNewsHolder.marqueeView.setOnItemClickListener(new com.gongwen.marqueen.util.OnItemClickListener() {
                @Override
                public void onItemClickListener(View mView, Object mData, int mPosition) {

                    mContext.startActivity(new Intent(mContext, NewsDetialActivity.class)
                            .putExtra("url", toplist.get(mPosition).getUrl())
                            .putExtra("title", toplist.get(mPosition).getTitle())
                            .putExtra("img", toplist.get(mPosition).getThumb())
                    );
                }
            });

        } else if (holder instanceof RecommendHolder) {
            RecommendHolder recommendHolder = (RecommendHolder) holder;
            Recommend_adapter adapter = new Recommend_adapter(recommendlist);
            recommendHolder.listView.setAdapter(adapter);
            recommendHolder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    mContext.startActivity(new Intent(mContext, VideoPlayerActivity.class)
                            .putExtra("url", recommendlist.get(position).getVideo())
                            .putExtra("title", recommendlist.get(position).getTitle())
                            .putExtra("img", recommendlist.get(position).getThumb()));
                }
            });

        } else if (holder instanceof LanmuHolder) {//频道
            final LanmuHolder lanmuHolder = (LanmuHolder) holder;
            //设置栏目
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 5);
            lanmuHolder.recyclerView.setLayoutManager(gridLayoutManager);
            Lanmu_adapter lanmu_adapter = new Lanmu_adapter(lanmulist);
            ((LanmuHolder) holder).recyclerView.setAdapter(lanmu_adapter);
            lanmu_adapter.setOnItemClickListener(new Lanmu_adapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (lanmulist.get(position).getMark().equals("czlx")) {//分类类型
                        mContext.startActivity(new Intent(mContext, ZWActivity.class)
                                .putExtra("url", lanmulist.get(position).getMarklinker())
                                .putExtra("mark", lanmulist.get(position).getMark())
                                .putExtra("title", lanmulist.get(position).getTitle()));
                    } else if (lanmulist.get(position).getMark().equals("zwxx")) {//列表类型
                        mContext.startActivity(new Intent(mContext, ZWActivity.class)
                                .putExtra("url", lanmulist.get(position).getMarklinker())
                                .putExtra("mark", lanmulist.get(position).getMark())
                                .putExtra("title", lanmulist.get(position).getTitle()));

                    } else if (lanmulist.get(position).getMark().equals("beutContry")) {//列表类型
                        mContext.startActivity(new Intent(mContext, ZWActivity.class)
                                .putExtra("url", lanmulist.get(position).getMarklinker())
                                .putExtra("mark", lanmulist.get(position).getMark())
                                .putExtra("title", lanmulist.get(position).getTitle()));


                    } else if (lanmulist.get(position).getMark().equals("ztzl")) {//分类类型
                        mContext.startActivity(new Intent(mContext, ZWActivity.class)
                                .putExtra("url", lanmulist.get(position).getMarklinker())
                                .putExtra("mark", lanmulist.get(position).getMark())
                                .putExtra("title", lanmulist.get(position).getTitle()));

                    } else if (lanmulist.get(position).getMark().equals("videolist")) {//分类类型

                        MainActivity.mLl3.performClick();//模拟人工触摸点击方式


                    }
                }
            });


        } else if (holder instanceof DslmHolder) {
            final DslmHolder dslmHolder = (DslmHolder) holder;
            //设置专题专栏
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            dslmHolder.recyclerView.setLayoutManager(linearLayoutManager);
            DSLM_adapter dslm_adapter = new DSLM_adapter(videolist);
            ((DslmHolder) holder).recyclerView.setAdapter(dslm_adapter);
            dslm_adapter.setOnItemClickListener(new DSLM_adapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    mContext.startActivity(new Intent(mContext, DianBoListActivity.class)
                            .putExtra("id", "ztzl")
                            .putExtra("mark", videolist.get(position).getIdentifier())
                            .putExtra("title", videolist.get(position).getTitle()));

                }
            });

        } else if (holder instanceof NewsListHolder) {//新闻列表 上方三个非列表的占位
            NewsListHolder newsListHolder = (NewsListHolder) holder;
            GlideUtils.load(newslist.get(position - 5).getThumb(), newsListHolder.imageView);
            newsListHolder.tv_1.setText(newslist.get(position - 5).getTitle());
            newsListHolder.tv_2.setText("阅 " + newslist.get(position - 5).getHits());
            newsListHolder.tv_4.setText(DateUtils.timeStampDate(newslist.get(position - 5).getDateline()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, NewsDetialActivity.class)
                            .putExtra("url", newslist.get(position - 5).getUrl())
                            .putExtra("title", newslist.get(position - 5).getTitle()));

                }
            });
        }
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_01;
        } else if (position == 1) {
            return ITEM_02;
        } else if (position == 2) {
            return ITEM_03;
        } else if (position == 3) {
            return ITEM_04;
        } else if (position == 4) {
            return ITEM_05;
        } else {
            return ITEM_06;
        }

    }

    @Override
    public int getItemCount() {
        return newslist.size() + 5;
    }


    /*****************************************下面是为不同的布局创建不同的ViewHolder*******************************************************/

    /**
     * 轮播图的viewholder
     */
    public static class BannerHolder extends RecyclerView.ViewHolder {
        RollPagerView rollPagerView;

        public BannerHolder(View itemView) {
            super(itemView);
            rollPagerView = itemView.findViewById(R.id.frag01_banner_news);


        }
    }

    /**
     * 头部滚动消息
     */
    public static class TopNewsHolder extends RecyclerView.ViewHolder {
        SimpleMarqueeView marqueeView;

        public TopNewsHolder(View itemView) {
            super(itemView);
            marqueeView = itemView.findViewById(R.id.marqueeView_topnews);


        }
    }

    /**
     * 推荐的viewholder
     */
    public static class RecommendHolder extends RecyclerView.ViewHolder {
        ListView listView;

        public RecommendHolder(View itemView) {
            super(itemView);
            listView = itemView.findViewById(R.id.listview_f1_recommend);


        }
    }

    /**
     * 栏目的viewholder
     */

    public static class LanmuHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        public LanmuHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_lanmu);
        }
    }

    /**
     * 视频列表的viewholder
     */

    public static class DslmHolder extends RecyclerView.ViewHolder {
        CustomVRecyclerView recyclerView;

        public DslmHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_dslm);
        }
    }

    /**
     * 新闻的viewholder
     */
    public static class NewsListHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tv_1, tv_2, tv_3, tv_4;

        public NewsListHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_item_news);
            tv_1 = itemView.findViewById(R.id.tv_item_news01);
            tv_2 = itemView.findViewById(R.id.tv_item_news02);
            tv_3 = itemView.findViewById(R.id.tv_item_news03);
            tv_4 = itemView.findViewById(R.id.tv_item_news04);
        }
    }

}
