package com.park.smet_k.bauman_gis.navigation;

import android.app.Application;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.park.smet_k.bauman_gis.R;
import com.park.smet_k.bauman_gis.model.GoPoint;
import com.park.smet_k.bauman_gis.model.GoRoute;

import java.util.ArrayList;
import java.util.List;

public class NavigatorViewModel extends AndroidViewModel {
    private NavigatorRepository mNavRepo = new NavigatorRepository(getApplication());
    private LiveData<GoRoute> mRoute = mNavRepo.getRoute();
    private LiveData<String> mError = mNavRepo.getError();

    public NavigatorViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<GoRoute> getRoute() {
        return mRoute;
    }

    public LiveData<String> getError() {
        return mError;
    }

    public void find(String from, String to) {
        mNavRepo.find(from, to);
    }

    public List<Pair<Bitmap, Integer>> buildBitMaps(Resources res, List<GoPoint> route) {
        Paint p = new Paint();
        Bitmap bitmapImg1 = BitmapFactory.decodeResource(res, R.drawable.bmstuplan);
        int width = bitmapImg1.getWidth();
        int height = bitmapImg1.getHeight();

        float multiplyH = (float) height / 1080;
        float multiplyW = (float) width / 1280;
        List<Pair<Bitmap, Integer>> pathList = new ArrayList<>();

        Bitmap bitmapImg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapImg);

        p.setColor(res.getColor(R.color.colorPrimary));
        p.setStrokeWidth(5);


        bitmapImg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmapImg);

        Integer level = route.get(0).getLevel();
        Integer newLevel = level;

        // TODO: поехало все
        for (int i = 0; i < route.size() - 1; i++) {
            if (!newLevel.equals(level)) {
                Bitmap merge = overlay(bitmapImg1, bitmapImg);
                pathList.add(new Pair<>(merge, level));

                bitmapImg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bitmapImg);
                level = newLevel;
            }

            if (route.get(i).getCabinet()) {
                p.setColor(res.getColor(R.color.colorPrimaryDark));
                p.setTextAlign(Paint.Align.RIGHT);
                p.setTextSize(40);
                canvas.drawText(route.get(i).getName(),
                        (route.get(i).getY()) * multiplyH + 160,
                        (route.get(i).getX()) * multiplyW + 1100,
                        p);

                p.setColor(res.getColor(R.color.colorAccent));

                canvas.drawCircle(
                        (route.get(i).getY() * multiplyH + 185),
                        (route.get(i).getX() * multiplyW + 1120),
                        30, p);

                p.setColor(res.getColor(R.color.colorPrimary));

            } else if (route.get(i).getStair()) {
                p.setColor(res.getColor(R.color.colorMiddle));
                canvas.drawCircle(
                        (route.get(i).getY() * multiplyH + 185),
                        (route.get(i).getX() * multiplyW + 1120),
                        20, p);
                p.setColor(res.getColor(R.color.colorPrimary));

            } else {
                canvas.drawCircle(
                        (route.get(i).getY() * multiplyH + 185),
                        (route.get(i).getX() * multiplyW + 1120),
                        20, p);
            }


            newLevel = route.get(i).getLevel();

        }

        Bitmap merge = overlay(bitmapImg1, bitmapImg);
        pathList.add(new Pair<>(merge, level));

        return pathList;
    }

    private Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, new Matrix(), null);
        return bmOverlay;
    }
}

