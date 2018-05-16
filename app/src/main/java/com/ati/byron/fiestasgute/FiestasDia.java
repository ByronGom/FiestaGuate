package com.ati.byron.fiestasgute;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.snowdream.android.widget.SmartImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.entity.mime.Header;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class FiestasDia extends Fragment {

    private ListView listView;

    ArrayList nombre = new ArrayList();
    ArrayList descripcion = new ArrayList();
    ArrayList fecha = new ArrayList();
    ArrayList fotografia = new ArrayList();
    ArrayList comunidad = new ArrayList();
    ArrayList departamento = new ArrayList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fiestas_dia, container, false);
        listView = (ListView) view.findViewById(R.id.listView);

        descargarImagen();


        return view;
    }

    private void descargarImagen(){
        nombre.clear();
        descripcion.clear();
        fecha.clear();
        fotografia.clear();
        comunidad.clear();
        departamento.clear();

        final ProgressDialog progressDialog =new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando Datos...");
        progressDialog.show();

        AsyncHttpClient client =new AsyncHttpClient();
        client.get("http://192.168.2.4/api/index.php", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                if (statusCode==200){
                    progressDialog.dismiss();
                    try {
                        JSONArray jsonArray = new JSONArray(new String(responseBody));
                        for (int i=0;i<jsonArray.length();i++){
                            nombre.add(jsonArray.getJSONObject(i).getString("Nombre_Fiestas"));
                            fecha.add(jsonArray.getJSONObject(i).getString("Fecha_Inicio"));
                            descripcion.add(jsonArray.getJSONObject(i).getString("Descripcion_Fiesta"));
                            fotografia.add(jsonArray.getJSONObject(i).getString("Fotografia"));
                            comunidad.add(jsonArray.getJSONObject(i).getString("Comunidad"));
                            departamento.add(jsonArray.getJSONObject(i).getString("Departamento"));


                        }

                        listView.setAdapter(new ImageAdapter(getContext()));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

            }


        });

    }

    private class ImageAdapter extends BaseAdapter {

        Context ctx;
        LayoutInflater layoutInflater;
        SmartImageView smartImageView;
        TextView tvnombre, tvdescripcion,tvfecha,tvcomunidad,tvdepartamento;


        public ImageAdapter(Context applicationContext) {
            this.ctx =applicationContext;
            layoutInflater=(LayoutInflater)ctx.getSystemService(LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return fotografia.size();

        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            @SuppressLint("ViewHolder") ViewGroup viewGroup=(ViewGroup)layoutInflater.inflate(R.layout.activity_main_item, null);

            smartImageView =(SmartImageView)viewGroup.findViewById(R.id.image1);
            tvnombre = (TextView)viewGroup.findViewById(R.id.tvnombre);
            tvfecha = (TextView)viewGroup.findViewById(R.id.tvfecha);
            tvdescripcion = (TextView)viewGroup.findViewById(R.id.tvdescription);
            tvcomunidad = (TextView)viewGroup.findViewById(R.id.tvcomunidad);
            tvdepartamento = (TextView)viewGroup.findViewById(R.id.tvdepartamento);

            String urlfinal=fotografia.get(position).toString();
            Rect rect = new Rect(smartImageView.getLeft(), smartImageView.getTop(), smartImageView.getRight(), smartImageView.getBottom());

            smartImageView.setImageUrl(urlfinal, rect);

            tvnombre.setText(nombre.get(position).toString());
            tvfecha.setText(fecha.get(position).toString());
            tvdescripcion.setText(descripcion.get(position).toString());
            tvcomunidad.setText(comunidad.get(position).toString());
            tvdepartamento.setText(departamento.get(position).toString());

            return viewGroup;
        }
    }


}
