package the.muisc;
import android.app.Activity;
import android.view.Window;
import android.widget.TextView;

import com.js.car.R;


public class DetailTitleBar {

	/**
	 * @see [自定义标题栏]
	 * @param activity
	 * @param title
	 */
	public static void getTitleBar(Activity activity,String title) {
		activity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		activity.setContentView(R.layout.detail_title);
		activity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.detail_title);
		TextView textView = (TextView) activity.findViewById(R.id.tv_songname);
		textView.setText(title);
	}
    public static void setWelcomePage(Activity activity){
    	activity.setContentView(R.layout.detail_title);
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
