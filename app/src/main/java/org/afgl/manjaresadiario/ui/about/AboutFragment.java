package org.afgl.manjaresadiario.ui.about;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

import org.afgl.manjaresadiario.R;
import org.afgl.manjaresadiario.lib.ImageLoader;
import org.afgl.manjaresadiario.ui.detail.OnButtonWebListener;
import org.afgl.manjaresadiario.utilities.InjectorUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment implements View.OnClickListener{

    private static final String EXTRA_TITLE = "title";
    private static final String URL = "https://afgl.neocities.org";

    private OnButtonWebListener listener;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnButtonWebListener){
            listener = (OnButtonWebListener) context;
        } else{
            throw new ClassCastException(context.toString() + "Debes implementar OnButtonWebListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Dibujo la foto circular
        ImageView imageView = view.findViewById(R.id.imagen_sagrario);
        ImageLoader loader = InjectorUtils.provideImageLoader(getContext());
        loader.loadCircle(imageView, R.drawable.sagrario);

        Button button = view.findViewById(R.id.button_licenses);
        button.setOnClickListener(this);

        Button buttonWeb = view.findViewById(R.id.button_web);
        buttonWeb.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch(id){
            case R.id.button_licenses:
                Intent intent = new Intent(getActivity(), OssLicensesMenuActivity.class);
                String title = getString(R.string.oss_license_title);
                intent.putExtra(EXTRA_TITLE, title);
                startActivity(intent);
                break;
            case R.id.button_web:
                listener.onButtonWebCliked();
                break;
        }
    }

    public void forwardToBrowser() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(URL);
        i.setData(uri);
        if(getActivity() != null) {
            List<ResolveInfo> activities = getActivity().getPackageManager().queryIntentActivities(i, 0);
            ArrayList<Intent> targetIntents = new ArrayList<>();
            String thisPackageName = getActivity().getApplicationContext().getPackageName();
            for(ResolveInfo currentInfo : activities){
                String packageName = currentInfo.activityInfo.packageName;
                if(!thisPackageName.equals(packageName)){
                    Intent targetIntent = new Intent(Intent.ACTION_VIEW);
                    targetIntent.setData(i.getData());
                    targetIntent.setPackage(packageName);
                    targetIntents.add(targetIntent);
                }
            }
            if(targetIntents.size() > 0){
                Intent chooserIntent = Intent.createChooser(targetIntents.remove(0), getString(R.string.chooser_web));
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetIntents.toArray(new Parcelable[]{}));
                startActivity(chooserIntent);
            }
        }
    }

}
