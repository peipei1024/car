package com.js.car.maptest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.js.car.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JiaM on 2016/5/9.
 */
public class ChooseLocation extends Activity implements TextWatcher, PoiSearch.OnPoiSearchListener {

    private EditText inputAddress;
    private String hintText;
    private ListView display_infor;
    private ImageView id_back_arrow_image;
    private PoiResult poiResult;//poi返回的结果
    private PoiSearch.Query query;//POI查询条件类
    private PoiSearch poiSearch;//POI搜索

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_location);
        initView();
    }

    private void initView() {
        inputAddress = (EditText) findViewById(R.id.choose_location);
        display_infor = (ListView) findViewById(R.id.display_infor);
        id_back_arrow_image = (ImageView) findViewById(R.id.id_back_arrow_image);
        inputAddress.addTextChangedListener(this);
        Intent intent = getIntent();
        hintText = intent.getStringExtra("hintText");
        inputAddress.setHint(hintText);
        id_back_arrow_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {

        query = new PoiSearch.Query(inputAddress.getText().toString(), "", "太原市");
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        String newText = s.toString().trim();
        Inputtips inputTips = new Inputtips(ChooseLocation.this,
                new InputtipsListener() {

                    @Override
                    public void onGetInputtips(List<Tip> tipList, int rCode) {
                        if (rCode == 1000) {// 正确返回

                            List<String> listString = new ArrayList<String>();
                            for (int i = 0; i < tipList.size(); i++) {
                                listString.add(tipList.get(i).getName());
                            }
                            doSearchQuery();
                           /* ArrayAdapter<String> list = new ArrayAdapter<String>(
                                    ChooseLocation.this,
                                    android.R.layout.simple_expandable_list_item_1, listString);
                            display_infor.setAdapter(list);
                            list.notifyDataSetChanged();*/
                        }
                    }
                });
        try {
            inputTips.requestInputtips(newText, "太原");// 第一个参数表示提示关键字，第二个参数默认代表全国，也可以为城市区号

        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    final List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                    List<String> listString = new ArrayList<String>();
                    for (int i = 0; i < poiItems.size(); i++) {
                        listString.add(poiItems.get(i).getTitle());
                    }
                    ChooseAdapter chooseAdapter = new ChooseAdapter(ChooseLocation.this, poiItems);
                    display_infor.setAdapter(chooseAdapter);
                    chooseAdapter.notifyDataSetChanged();
                    display_infor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent back = new Intent();
                            back.putExtra("address", poiItems.get(position).getTitle());
                            back.putExtra("Longitude", poiItems.get(position).getLatLonPoint().getLongitude());
                            back.putExtra("Latitude", poiItems.get(position).getLatLonPoint().getLatitude());
                            setResult(2, back);
                            finish();
                        }
                    });
                }
            } else {
                ToastUtil.show(ChooseLocation.this,
                        R.string.no_result);
            }
        } else if (rCode == 27) {
            ToastUtil.show(ChooseLocation.this,
                    R.string.error_network);
        } else if (rCode == 32) {
            ToastUtil.show(ChooseLocation.this, R.string.error_key);
        } else {
            ToastUtil.show(ChooseLocation.this, getString(R.string.error_other) + rCode);
        }

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}
