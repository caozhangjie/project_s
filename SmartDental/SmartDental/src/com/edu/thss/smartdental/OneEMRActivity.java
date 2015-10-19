package com.edu.thss.smartdental;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

public class OneEMRActivity extends Activity {
	private WebView emrWebView;
	private Handler emrHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_one_emr);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_bg));
		
		this.emrWebView = (WebView)findViewById(R.id.emr_table);
		WebSettings webSettings = this.emrWebView.getSettings(); 
		webSettings.setBuiltInZoomControls(true); 
		webSettings.setSupportZoom(true);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		this.emrWebView.setHorizontalScrollBarEnabled(true);
		
		/*webSettings.setJavaScriptEnabled(true);
		this.emrWebView.addJavascriptInterface(new Object(){
			public void clickOnAndroid(){
				emrHandler.post(new Runnable(){

					@Override
					public void run() {
						emrWebView.loadUrl("javascript:function()");
						
					}
					
				});
			}
		}, "table");
         */
		//判断病历类型
		int  emrclass = getIntent().getExtras().getInt("emrclass");
		if(emrclass==2){
			this.emrWebView.loadUrl("file:///android_asset/table2.html");
		}
		else if(emrclass == 3){
			this.emrWebView.loadUrl("file:///android_asset/table3.html");
		}
		this.emrWebView.getSettings().setDefaultTextEncodingName("utf-8") ;
	//	this.emrWebView.loadData(creatHtml(), "text/html", "utf-8");  
	//	this.emrWebView.loadDataWithBaseURL(null,creatHtml(), "text/html", "utf-8",null);
	}
    private String creatHtml(){
    	StringBuffer sb = new StringBuffer();
    	sb.append("<html>").append("<head>");
    	sb.append("<meta charset=\"utf-8\"/>"); 
    	sb.append("</head><style type=\"text/css\">");
    	sb.append(".title{font-size: 30px;font-weight: bolder;}");
    	sb.append(".subtitle{font-size: 20px;font-weight: bolder;/*background-color: #ECF1EF;   */}");
    	sb.append(".basicInfo{background-color: #FFFFF2;}");
    	sb.append(".additionalInfo{background-color:#FBF5E6;}");
    	sb.append(".history{background-color:#FCF6CF;}");
    	sb.append(".visit{background-color:#effff8;}");
    	sb.append(".cost{background-color:#efffef}");
    	sb.append(".result{background-color:#eff8ff}");
    	sb.append(".surgery{background-color:#efefff}");
    	sb.append(".anesthesia{background-color:#fff8ef}");
    	sb.append(".hospital{background-color:#fff4f4}");
    	sb.append(".prescription{background-color:#ffffef}");
    	sb.append("</style><body style=\"width=600px; height=auto\">");
    	sb.append("<table width=\"100%\"border=\"1\" cellspacing=\"0px\" style=\"border-collapse:collapse;font-family:Microsoft YaHei\">");
    	sb.append("<caption class=\"title\">电子病历表单</caption>");
    	sb.append("<tr><td class=\"subtitle\" colspan=\"6\">电子病历首页</td></tr>");
    	sb.append("<tr class=\"basicInfo\">");
    	sb.append("<td width=\"13.3%\">姓名</td>");
    	sb.append("<td width=\"13.3%\">&nbsp;</td>"); //!!
    	sb.append("<td width=\"13.3%\">性别</td>");
    	sb.append("<td width=\"13.3%\">&nbsp;</td>"); //!!
    	sb.append("<td width=\"13.3%\">出生日期</td>");
    	sb.append("<td width=\"13.3%\">&nbsp;</td>"); //!!
    	sb.append("</tr><tr class=\"basicInfo\">");
    	sb.append("<td>身份证号</td>");
    	sb.append("<td colspan=\"2\">&nbsp;</td>"); //!!
    	sb.append("<td>医保号</td>");
    	sb.append("<td colspan=\"2\">&nbsp;</td>"); //
    	sb.append("</tr><tr class=\"basicInfo\">");
    	sb.append("<td rowspan=\"4\">联系方式</td>");
    	sb.append("<td colspan=\"1\">家庭住址</td>");
    	sb.append("<td colspan=\"4\">&nbsp;</td>"); //
    	sb.append("</tr><tr class=\"basicInfo\">");
    	sb.append("<td colspan=\"1\">家庭电话</td>");
    	sb.append("<td colspan=\"4\">&nbsp;</td>"); //
    	sb.append("</tr><tr class=\"basicInfo\">");
    	sb.append("<td colspan=\"1\">手机</td>");
    	sb.append("<td colspan=\"4\">&nbsp;</td>"); //
    	sb.append("</tr><tr class=\"basicInfo\">");
    	sb.append("<td colspan=\"1\">Email</td>");
    	sb.append("<td colspan=\"4\">&nbsp;</td>"); //
    	sb.append("</tr><tr class=\"additionalInfo\">");
    	sb.append("<td>国籍</td>");
    	sb.append("<td>&nbsp;</td>");//
    	sb.append("<td>民族</td>");
    	sb.append("<td>&nbsp;</td>");//
    	sb.append("<td>血型</td>");
    	sb.append("<td>&nbsp;</td>"); //
    	sb.append("</tr><tr class=\"additionalInfo\">");
    	sb.append("<td>职业</td>");
    	sb.append("<td colspan=\"2\">&nbsp;</td>");//
    	sb.append("<td>受教育程度</td>");
    	sb.append("<td colspan=\"2\">&nbsp;</td>");//
    	sb.append("</tr><tr class=\"additionalInfo\">");
    	sb.append("<td rowspan=\"2\">其他联系方式</td>");
    	sb.append("<td>家庭住址</td>");
    	sb.append("<td colspan=\"4\">&nbsp;</td>");//
    	sb.append("</tr><tr class=\"additionalInfo\">");
    	sb.append("<td>家庭电话</td>");
    	sb.append("<td colspan=\"4\">&nbsp;</td>");//
    	sb.append("</tr><tr class=\"history\">");
    	sb.append("<td>既往史</td>");
    	sb.append("<td colspan=\"5\">&nbsp;</td>");//
    	sb.append("</tr><tr class=\"history\">");
    	sb.append("<td>过敏史</td>");
    	sb.append("<td colspan=\"5\">&nbsp;</td>");//
    	sb.append("</tr><tr class=\"history\">");
    	sb.append("<td>家族史</td>");
    	sb.append("<td colspan=\"5\">&nbsp;</td>");//
    	sb.append("</tr><tr>");
    	sb.append("<td class=\"subtitle\"colspan=\"6\">就诊信息</td></tr>");
    	sb.append("<tr class=\"visit\">");
    	sb.append("<td>就诊时间</td>");
    	sb.append("<td colspan=\"2\">&nbsp;</td>"); // 
    	sb.append("<td>病案号</td>");
    	sb.append("<td colspan=\"2\">&nbsp;</td>");
    	sb.append("</tr><tr>");
    	sb.append("<td class=\"subtitle\" colspan=\"6\">费用</td></tr>");
    	sb.append("<tr class=\"cost\">");
    	sb.append("<td>总费用</td>");
    	sb.append("<td colspan=\"2\">&nbsp;</td>");
    	sb.append("<td>是否交款</td>");
    	sb.append("<td colspan=\"2\">&nbsp;</td>");
    	sb.append("</tr><tr class=\"cost\">");
    	sb.append("<td>明细</td>");
    	sb.append("<td colspan=\"5\"><table width=\"100%\"><tr>");
    	sb.append("<th>类别</th><th>项目</th><th>单价</th><th>数量</th><th>合计</th></tr><tr>");
    	sb.append("<td>&nbsp;</tr>");
    	sb.append("<td>&nbsp;</tr>");
    	sb.append("<td>&nbsp;</tr>");
    	sb.append("<td>&nbsp;</tr>");
    	sb.append("<td>&nbsp;</tr>");
    	sb.append("</tr></table></td></tr><tr>");
    	sb.append("<td class=\"subtitle\" colspan=\"6\">就诊结果</td>");
    	sb.append("</tr><tr class=\"result\">");
    	sb.append("<td>科室</td>");
    	sb.append("<td>&nbsp;</td>"); //
    	sb.append("<td>医生</td>");
    	sb.append("<td>&nbsp;</td>");
    	sb.append("<td>护士</td>");
    	sb.append("<td>&nbsp;</td>");
    	sb.append("</tr><tr class=\"result\">");
    	sb.append("<td>主诉</td>");
    	sb.append("<td colspan=\"5\">&nbsp;</td>"); //
    	sb.append("</tr><tr class=\"result\">");
    	sb.append("<td>检查结果</td>");
    	sb.append("<td colspan=\"5\">&nbsp;</td>");
    	sb.append("</tr><tr class=\"result\">");
    	sb.append("<td>诊断结果</td>");
    	sb.append("<td colspan=\"5\">&nbsp;</td>");
    	sb.append("</tr><tr class=\"result\">");
    	sb.append("<td>治疗计划</td>");
    	sb.append("<td colspan=\"5\">&nbsp;</td>");
    	sb.append("</tr><tr class=\"result\">");
    	sb.append("<td>医嘱</td>");
    	sb.append("<td colspan=\"5\">&nbsp;</td>");//
    	sb.append("</tr><tr>");
    	sb.append("<td class=\"subtitle\"colspan=\"6\">一般手术记录</td></tr>");
    	sb.append("<tr class=\"surgery\"><td colspan=\"6\">&nbsp;</td></tr>");
    	sb.append("<tr><td class=\"subtitle\"colspan=\"6\">麻醉手术记录</td></tr>");
    	sb.append("<tr class=\"anesthesia\">");
    	sb.append("<td>术前访视记录</td>");
    	sb.append("<td colspan=\"5\">&nbsp;</td>"); //
    	sb.append("</tr><tr class=\"anesthesia\">");
    	sb.append("<td>麻醉记录</td>");
    	sb.append("<td colspan=\"5\">&nbsp;</td>");//
    	sb.append("</tr><tr class=\"anesthesia\">");
    	sb.append("<td>术后访视记录</td>");
    	sb.append("<td colspan=\"5\">&nbsp;</td>");
    	sb.append("</tr><tr>");
    	sb.append("<td class=\"subtitle\" colspan=\"6\">住院记录</td></tr>");
    	sb.append("<tr class=\"hospital\">");
    	sb.append("<td>入院记录</td>");
    	sb.append("<td colspan=\"5\">&nbsp;</td>");
    	sb.append("</tr><tr class=\"hospital\">");
    	sb.append("<td>日常病程记录</td>");
    	sb.append("<td colspan=\"5\">&nbsp;</td>");
    	sb.append("</tr><tr class=\"hospital\">");
    	sb.append("<td>出院记录</td>");
    	sb.append("<td colspan=\"5\">&nbsp;</td>");
    	sb.append("</tr><tr>");
    	sb.append("<td class=\"subtitle\"colspan=\"6\">处方</td></tr>");
    	sb.append("<tr class=\"prescription\"><td colspan=\"6\">&nbsp;</td></tr>");
    	sb.append("</table></body></html>");
    	
    	return sb.toString();
    }

}
