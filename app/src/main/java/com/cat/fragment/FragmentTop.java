package com.cat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.cat.activity.InfoActivity;

import com.cat.R;
import com.cat.entity.Travel;
import com.devspark.robototextview.widget.RobotoTextView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;



public class FragmentTop extends Fragment {

    static final String ARG_TRAVEL = "ARG_TRAVEL";
    Travel travel;

    @BindView(R.id.image) ImageView image;
    @BindView(R.id.author)
    RobotoTextView author;
    @BindView(R.id.isbn)RobotoTextView isbn;



    public static FragmentTop newInstance(Travel travel) {
        Bundle args = new Bundle();
        FragmentTop fragmentTop = new FragmentTop();
        args.putParcelable(ARG_TRAVEL, travel);
        fragmentTop.setArguments(args);
        return fragmentTop;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            travel = args.getParcelable(ARG_TRAVEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_front, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (travel != null) {
            Picasso.with(getContext()).load(travel.getImage()).placeholder(R.drawable.default_image).into(image);
            author.setText("书架主人："+travel.getUserName());
            isbn.setText("书架编号：NO."+travel.getStoreID());
        }
    }

    @SuppressWarnings("unchecked")
    private void startInfoActivity(View view, Travel travel) {
        FragmentActivity activity = getActivity();
        ActivityCompat.startActivity(activity,
            InfoActivity.newInstance(activity, travel),
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,
                new Pair<>(view, getString(R.string.transition_image)))
                .toBundle());
    }
}
