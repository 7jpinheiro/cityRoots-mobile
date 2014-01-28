package com.uminho.uce15.cityroots;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.uminho.uce15.cityroots.data.Event;

import java.util.ArrayList;

/**
 * Created by lgomes on 28-01-2014.
 */
public class AdsListview extends Fragment {

        private ListView list ;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_search, container, false);
            list = (ListView) rootView.findViewById(R.id.listView_ads);
            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            Event event = new Event("Braga",null,null,null,null,null,null,0,0,true,0,null,true,null,null,0,null,0,null,null,null,null,null);

            ArrayList lista = new ArrayList();
            lista.add(event);
            ListAdapter adapter = new ListAdapter(getActivity(), lista);
            list.setAdapter(adapter);
            list.invalidate();
        }

        @Override
        public void onResume() {
            super.onResume();
            ArrayList lista = new ArrayList();
            Event event = new Event("Braga",null,null,null,null,null,null,0,0,true,0,null,true,null,null,0,null,0,null,null,null,null,null);
            lista.add(event);
            ListAdapter adapter = new ListAdapter(getActivity(), lista);
            list.setAdapter(adapter);
            list.invalidate();
        }

}
