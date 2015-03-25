package gl.android.widget.viewpager;

import android.R;
import android.os.Handler;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;

import com.jfeinstein.jazzyviewpager.JazzyViewPager;
import com.jfeinstein.jazzyviewpager.JazzyViewPager.TransitionEffect;
import com.viewpagerindicator.PageIndicator;

public class Banner implements OnPageChangeListener {
	private PageIndicator mPageIndicator;
	private JazzyViewPager mViewPager;
	Handler handler;
	private TextView descView;

	public Banner(JazzyViewPager viewpager, PageIndicator pPageIndicator,
			TextView view) {
		this.mPageIndicator = pPageIndicator;
		this.mViewPager = viewpager;
		setupJazziness(TransitionEffect.CubeIn);
		setViewPagerIndicator(viewpager);
		mViewPager.setOnPageChangeListener(this);
		handler = new Handler();
		this.descView = view;
		mViewPager.setOnPagerTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_CANCEL
						|| event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					startLoop();
				} else {
					stopLoop();
				}
				return false;
			}
		});

	}

	public void setupJazziness(TransitionEffect effect) {
		if (mViewPager instanceof JazzyViewPager) {
			JazzyViewPager mJazzy = (JazzyViewPager) mViewPager;
			mJazzy.setTransitionEffect(effect);
			mJazzy.setPageMargin(30);
		}
	}

	public void setViewPagerIndicator(JazzyViewPager viewPager) {
		mPageIndicator.setViewPager(viewPager);

	}

	public void stopLoop() {
		handler.removeCallbacksAndMessages(null);

	}

	int nextIndex = 0;
	int delayMillis = 5000;

	public void startLoop() {
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				boolean isSmoll = true;
				if (mViewPager != null) {

					int size = 0;
					if (mViewPager.getAdapter() != null) {
						size = mViewPager.getAdapter().getCount();
					}
					if (mViewPager.getCurrentItem() + 1 < size) {
						nextIndex = mViewPager.getCurrentItem() + 1;
					} else {
						isSmoll = false;
						nextIndex = 0;
					}
					mViewPager.setCurrentItem(nextIndex, isSmoll);
				}

				handler.postDelayed(this, delayMillis);
			}
		}, delayMillis);

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

		if (arg0 == OnScrollListener.SCROLL_STATE_IDLE) {
			if (mViewPager.getAdapter() != null) {
				CharSequence pageTitle = mViewPager.getAdapter().getPageTitle(
						mViewPager.getCurrentItem());
				if (!TextUtils.isEmpty(pageTitle)) {
					descView.setText(pageTitle);
					descView.startAnimation(AnimationUtils.loadAnimation(
							descView.getContext(), R.anim.fade_in));
					descView.setVisibility(View.VISIBLE);
				} else {
					descView.setVisibility(View.GONE);
				}
			}

		} else if (arg0 == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
				|| arg0 == OnScrollListener.SCROLL_STATE_FLING) {
			descView.startAnimation(AnimationUtils.loadAnimation(
					descView.getContext(), R.anim.fade_out));
			descView.setVisibility(View.GONE);
		}

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub

	}
}
