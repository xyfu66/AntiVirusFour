package com.jinyun.antivirusfour.newsReader.function;

import android.util.Log;

import com.jinyun.antivirusfour.newsReader.bean.NewsItemModel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析新闻数据
 * 抓取目标URL地址：http://news.qq.com/china_index.shtml
 *
 * ([^\"]*),([^</a>]*),([^</p>]*)这三个比较奇怪的语句，
 * 在此限定的字符串中任意匹配所有字符直到遇到\”结束。
 * 其它两个([^</a>]*),([^</p>]*)也差不多同样的意思。
 *
 */

public class Function {
    public static List<NewsItemModel> parseHtmlData(String result) {
        List<NewsItemModel> list = new ArrayList<>();

        Pattern pattern = Pattern
                .compile("<a target=\"_blank\" class=\"pic\" href=\"([^\"]*)\"><img class=\"picto\" src=\"([^\"]*)\"></a><em class=\"f14 l24\"><a target=\"_blank\" class=\"linkto\" href=\"[^\"]*\">([^</a>]*)</a></em><p class=\"l22\">([^</p>]*)</p>");
        Matcher matcher = pattern.matcher(result);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            NewsItemModel model = new NewsItemModel();
            model.setNewsDetailUrl(matcher.group(1).trim());
            model.setUrlImgAddress(matcher.group(2).trim());
            model.setNewsTitle(matcher.group(3).trim());
            model.setNewsSummary(matcher.group(4).trim());

            sb.append("详情页地址：" + matcher.group(1).trim() + "\n");
            sb.append("图片地址：" + matcher.group(2).trim() + "\n");
            sb.append("标题：" + matcher.group(3).trim() + "\n");
            sb.append("概要：" + matcher.group(4).trim() + "\n\n");

            list.add(model);
        }

        Log.e("----------------->", sb.toString());

        return list;
    }
}
