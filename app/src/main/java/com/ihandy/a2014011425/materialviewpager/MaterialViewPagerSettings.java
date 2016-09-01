package com.ihandy.a2014011425.materialviewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;

import static com.ihandy.a2014011425.materialviewpager.Utils.pxToDp;

/**
 * Created by florentchampigny on 29/04/15.
 * <p/>
 * Save attributes given to MaterialViewPager from layout
 */
public class MaterialViewPagerSettings implements Parcelable {

    //attributes are protected and can be used by class from the same package
    //com.ihandy.a2014011425.materialviewpager

    public static final Creator<com.ihandy.a2014011425.materialviewpager.MaterialViewPagerSettings> CREATOR = new Creator<com.ihandy.a2014011425.materialviewpager.MaterialViewPagerSettings>() {
        public com.ihandy.a2014011425.materialviewpager.MaterialViewPagerSettings createFromParcel(Parcel source) {
            return new com.ihandy.a2014011425.materialviewpager.MaterialViewPagerSettings(source);
        }

        public com.ihandy.a2014011425.materialviewpager.MaterialViewPagerSettings[] newArray(int size) {
            return new com.ihandy.a2014011425.materialviewpager.MaterialViewPagerSettings[size];
        }
    };
    protected int headerLayoutId;
    protected int pagerTitleStripId;
    protected int viewpagerId;
    protected int logoLayoutId;
    protected int logoMarginTop;
    protected int headerAdditionalHeight;
    protected int headerHeight;
    protected int headerHeightPx;
    protected int color;
    protected float headerAlpha;
    protected float parallaxHeaderFactor;
    protected float imageHeaderDarkLayerAlpha;
    protected boolean hideToolbarAndTitle;
    protected boolean hideLogoWithFade;
    protected boolean enableToolbarElevation;
    protected boolean displayToolbarWhenSwipe;
    protected boolean toolbarTransparent;
    protected boolean animatedHeaderImage;
    protected boolean disableToolbar;

    //region parcelable

    public MaterialViewPagerSettings() {
    }

    private MaterialViewPagerSettings(Parcel in) {
        this.headerLayoutId = in.readInt();
        this.pagerTitleStripId = in.readInt();
        this.viewpagerId = in.readInt();
        this.logoLayoutId = in.readInt();
        this.logoMarginTop = in.readInt();
        this.headerAdditionalHeight = in.readInt();
        this.headerHeight = in.readInt();
        this.headerHeightPx = in.readInt();
        this.color = in.readInt();
        this.headerAlpha = in.readFloat();
        this.imageHeaderDarkLayerAlpha = in.readFloat();
        this.parallaxHeaderFactor = in.readFloat();
        this.hideToolbarAndTitle = in.readByte() != 0;
        this.hideLogoWithFade = in.readByte() != 0;
        this.enableToolbarElevation = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.headerLayoutId);
        dest.writeInt(this.pagerTitleStripId);
        dest.writeInt(this.viewpagerId);
        dest.writeInt(this.logoLayoutId);
        dest.writeInt(this.logoMarginTop);
        dest.writeInt(this.headerAdditionalHeight);
        dest.writeInt(this.headerHeight);
        dest.writeInt(this.headerHeightPx);
        dest.writeInt(this.color);
        dest.writeFloat(this.headerAlpha);
        dest.writeFloat(this.imageHeaderDarkLayerAlpha);
        dest.writeFloat(this.parallaxHeaderFactor);
        dest.writeByte(hideToolbarAndTitle ? (byte) 1 : (byte) 0);
        dest.writeByte(hideLogoWithFade ? (byte) 1 : (byte) 0);
        dest.writeByte(enableToolbarElevation ? (byte) 1 : (byte) 0);
    }

    /**
     * Retrieve attributes from the MaterialViewPager
     *
     * @param context
     * @param attrs
     */
    protected void handleAttributes(Context context, AttributeSet attrs) {
        try {
            TypedArray styledAttrs = context.obtainStyledAttributes(attrs, com.ihandy.a2014011425.R.styleable.MaterialViewPager);
            {
                headerLayoutId = styledAttrs.getResourceId(com.ihandy.a2014011425.R.styleable.MaterialViewPager_viewpager_header, -1);
            }
            {
                pagerTitleStripId = styledAttrs.getResourceId(com.ihandy.a2014011425.R.styleable.MaterialViewPager_viewpager_pagerTitleStrip, -1);
                if (pagerTitleStripId == -1) {
                    pagerTitleStripId = com.ihandy.a2014011425.R.layout.material_view_pager_pagertitlestrip_standard;
                }
            }
            {
                viewpagerId = styledAttrs.getResourceId(com.ihandy.a2014011425.R.styleable.MaterialViewPager_viewpager_viewpager, -1);
            }
            {
                logoLayoutId = styledAttrs.getResourceId(com.ihandy.a2014011425.R.styleable.MaterialViewPager_viewpager_logo, -1);
                logoMarginTop = styledAttrs.getDimensionPixelSize(com.ihandy.a2014011425.R.styleable.MaterialViewPager_viewpager_logoMarginTop, 0);
            }
            {
                color = styledAttrs.getColor(com.ihandy.a2014011425.R.styleable.MaterialViewPager_viewpager_color, 0);
            }
            {
                headerHeightPx = styledAttrs.getDimensionPixelOffset(com.ihandy.a2014011425.R.styleable.MaterialViewPager_viewpager_headerHeight, 200);
                headerHeight = Math.round(pxToDp(headerHeightPx, context)); //convert to dp
            }
            {
                headerAdditionalHeight = styledAttrs.getDimensionPixelOffset(com.ihandy.a2014011425.R.styleable.MaterialViewPager_viewpager_headerAdditionalHeight, 60);
            }
            {
                headerAlpha = styledAttrs.getFloat(com.ihandy.a2014011425.R.styleable.MaterialViewPager_viewpager_headerAlpha, 0.5f);
            }
            {
                imageHeaderDarkLayerAlpha = styledAttrs.getFloat(com.ihandy.a2014011425.R.styleable.MaterialViewPager_viewpager_imageHeaderDarkLayerAlpha, 0.0f);
            }
            {
                parallaxHeaderFactor = styledAttrs.getFloat(com.ihandy.a2014011425.R.styleable.MaterialViewPager_viewpager_parallaxHeaderFactor, 1.5f);
                parallaxHeaderFactor = Math.max(parallaxHeaderFactor, 1); //min=1
            }
            {
                hideToolbarAndTitle = styledAttrs.getBoolean(com.ihandy.a2014011425.R.styleable.MaterialViewPager_viewpager_hideToolbarAndTitle, false);
                hideLogoWithFade = styledAttrs.getBoolean(com.ihandy.a2014011425.R.styleable.MaterialViewPager_viewpager_hideLogoWithFade, false);
            }
            {
                enableToolbarElevation = styledAttrs.getBoolean(com.ihandy.a2014011425.R.styleable.MaterialViewPager_viewpager_enableToolbarElevation, false);
            }
            {
                displayToolbarWhenSwipe = styledAttrs.getBoolean(com.ihandy.a2014011425.R.styleable.MaterialViewPager_viewpager_displayToolbarWhenSwipe, false);
            }
            {
                toolbarTransparent = styledAttrs.getBoolean(com.ihandy.a2014011425.R.styleable.MaterialViewPager_viewpager_transparentToolbar, false);
            }
            {
                animatedHeaderImage = styledAttrs.getBoolean(com.ihandy.a2014011425.R.styleable.MaterialViewPager_viewpager_animatedHeaderImage, true);
            }
            {
                disableToolbar = styledAttrs.getBoolean(com.ihandy.a2014011425.R.styleable.MaterialViewPager_viewpager_disableToolbar, false);
            }
            styledAttrs.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //endregion
}
