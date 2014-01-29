package com.uminho.uce15.cityroots;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.uminho.uce15.cityroots.data.Event;

import java.util.ArrayList;
import java.util.List;

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
            DataProvider provider = new DataProvider(getActivity().getApplicationContext()) ;

            List<Event> lista = new ArrayList<Event>();
            lista = provider.getEvents("Anuncio");
            ListAdapter adapter = new ListAdapter(getActivity(), lista);
            list.setAdapter(adapter);
            list.invalidate();
        }

        @Override
        public void onResume() {
            super.onResume();
            DataProvider provider = new DataProvider(getActivity().getApplicationContext()) ;

            List<Event> lista = new ArrayList<Event>();
            lista = provider.getEvents("Anuncio");
            ListAdapter adapter = new ListAdapter(getActivity(), lista);
            list.setAdapter(adapter);
            list.invalidate();
        }

}
