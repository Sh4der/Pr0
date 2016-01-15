package com.pr0gramm.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.common.base.Enums;
import com.google.common.primitives.Ints;
import com.pr0gramm.app.feed.ContentType;
import com.pr0gramm.app.ui.Themes;
import com.pr0gramm.app.ui.fragments.IndicatorStyle;

import java.util.EnumSet;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.Objects.equal;

/**
 */
@Singleton
public class Settings {
    private final SharedPreferences preferences;

    @Inject
    public Settings(Context context) {
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private Settings(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public boolean getContentTypeSfw() {
        return preferences.getBoolean("pref_feed_type_sfw", true);
    }

    public boolean getContentTypeNsfw() {
        return preferences.getBoolean("pref_feed_type_nsfw", false);
    }

    public boolean getContentTypeNsfl() {
        return preferences.getBoolean("pref_feed_type_nsfl", false);
    }

    /**
     * Gets a set of all selected content types.
     */
    public EnumSet<ContentType> getContentType() {
        EnumSet<ContentType> result = EnumSet.allOf(ContentType.class);

        if (!getContentTypeNsfl())
            result.remove(ContentType.NSFL);

        if (!getContentTypeNsfw())
            result.remove(ContentType.NSFW);

        // leave at least sfw.
        if (result.size() > 1 && !getContentTypeSfw())
            result.remove(ContentType.SFW);

        return result;
    }

    public boolean analyticsEnabled() {
        return preferences.getBoolean("pref_crashlytics_enabled", true);
    }

    public boolean convertGifToWebm() {
        return preferences.getBoolean("pref_convert_gif_to_webm", false);
    }

    public String downloadLocation(Context context) {
        String def = context.getString(R.string.pref_downloadLocation_default);
        return preferences.getString("pref_downloadLocation", def);
    }

    public IndicatorStyle seenIndicatorStyle() {
        String value = preferences.getString("pref_seen_indicator_style", IndicatorStyle.NONE.toString());

        assert value != null;
        return Enums.getIfPresent(IndicatorStyle.class, value).or(IndicatorStyle.NONE);
    }

    public boolean doubleTapToUpvote() {
        return preferences.getBoolean("pref_double_tap_to_upvote", true);
    }

    public boolean fullscreenZoomView() {
        return preferences.getBoolean("pref_fullscreen_zoom_view", true);
    }

    public boolean showPinButton() {
        return preferences.getBoolean("pref_show_pin_button", true);
    }

    public boolean showRefreshButton() {
        return preferences.getBoolean("pref_show_refresh_button", true);
    }

    public boolean useBetaChannel() {
        return preferences.getBoolean("pref_use_beta_channel", false);
    }

    public boolean showNotifications() {
        return preferences.getBoolean("pref_show_notifications", true);
    }

    public boolean useProxy() {
        return preferences.getBoolean("pref_use_proxy", true);
    }

    public boolean keepScreenOn() {
        return preferences.getBoolean("pref_keep_screen_on", true);
    }

    public ConfirmOnMobile confirmPlayOnMobile(Context context) {
        String prefValue = preferences.getString("pref_confirm_play_on_mobile_list", null);
        if (prefValue == null) {
            prefValue = context.getString(R.string.pref_confirm_play_on_mobile_default);
        }

        for (ConfirmOnMobile enumValue : ConfirmOnMobile.values()) {
            if (equal(context.getString(enumValue.value), prefValue)) {
                return enumValue;
            }
        }

        return ConfirmOnMobile.VIDEO;
    }

    public boolean loadHqInZoomView() {
        return preferences.getBoolean("pref_load_hq_image_in_zoomview", false);
    }

    public boolean hideTagVoteButtons() {
        return preferences.getBoolean("pref_hide_tag_vote_buttons", false);
    }

    public boolean tagCloudView() {
        return preferences.getBoolean("pref_tag_cloud_view", false);
    }

    public boolean useHttps() {
        return preferences.getBoolean("pref_use_https", false);
    }

    public boolean useSoftwareDecoder() {
        return preferences.getBoolean("pref_use_software_decoder", false);
    }

    public boolean forceMpegDecoder() {
        return preferences.getBoolean("pref_force_mpeg_decoder", false);
    }

    public boolean markRepostsInFeed() {
        return preferences.getBoolean("pref_show_repost_hint", true);
    }

    public boolean showGoogleImageButton() {
        return preferences.getBoolean("pref_show_google_image_button", true);
    }

    public boolean feedStartAtNew() {
        return preferences.getBoolean("pref_feed_start_at_new", false);
    }

    public boolean singleTapForFullscreen() {
        return preferences.getBoolean("pref_single_tap_for_fullscreen", false);
    }

    public boolean prioritizeOpComments() {
        return preferences.getBoolean("pref_prioritize_op_comments", true);
    }

    public boolean showCategoryRandom() {
        return preferences.getBoolean("pref_show_category_random", true);
    }

    public boolean showCategoryControversial() {
        return preferences.getBoolean("pref_show_category_controversial", true);
    }

    public boolean showCategoryPremium() {
        return preferences.getBoolean("pref_show_category_premium", true);
    }

    public int bestOfBenisThreshold() {
        String value = preferences.getString("pref_bestof_threshold", "2000");
        return firstNonNull(Ints.tryParse(value), 0);
    }

    public String themeName() {
        return preferences.getString("pref_theme", Themes.ORANGE.name());
    }

    public boolean showCategoryBestOf() {
        return bestOfBenisThreshold() > 0;
    }

    public boolean mockApi() {
        return BuildConfig.DEBUG && preferences.getBoolean("pref_debug_mock_api", false);
    }

    public boolean useApiProxy() {
        return preferences.getBoolean("pref_use_api_proxy", false);
    }

    public SharedPreferences.Editor edit() {
        return preferences.edit();
    }

    public SharedPreferences raw() {
        return preferences;
    }

    public static Settings of(Context context) {
        return new Settings(context.getApplicationContext());
    }

    public static Settings of(SharedPreferences preferences) {
        return new Settings(preferences);
    }

    public static void resetContentTypeSettings(Settings settings) {
        // reset settings.
        settings.preferences.edit()
                .putBoolean("pref_feed_type_sfw", true)
                .putBoolean("pref_feed_type_nsfw", false)
                .putBoolean("pref_feed_type_nsfl", false)
                .apply();
    }

    public enum ConfirmOnMobile {
        NONE(R.string.pref_confirm_play_on_mobile_values__play_direct),
        VIDEO(R.string.pref_confirm_play_on_mobile_values__video_only),
        ALL(R.string.pref_confirm_play_on_mobile_values__all);

        final int value;

        ConfirmOnMobile(int stringValue) {
            this.value = stringValue;
        }
    }
}
