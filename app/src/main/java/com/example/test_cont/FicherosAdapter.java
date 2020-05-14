package com.example.test_cont;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class FicherosAdapter extends RecyclerView.Adapter<FicherosAdapter.mViewHolder> {

    private Context context;
    private ArrayList<directorio_link> listaCarpetas;
    FirebaseStorage mfirebaseStorage;
    StorageReference mstorageReference;


    public FicherosAdapter(Context c, ArrayList<directorio_link> listaCarpetas){
    this.context=c;
    this.listaCarpetas=listaCarpetas;
    }

    public FicherosAdapter() {
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false);

        mfirebaseStorage = FirebaseStorage.getInstance();
        mstorageReference = mfirebaseStorage.getReference();
        return new mViewHolder(view);
    }
    //pasar datos al recyclerView del fragmento FILES y poner un onclicklistener para descargar


    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, final int position) {
        final directorio_link mlista=this.listaCarpetas.get(position);

        holder.folderName.setText(mlista.getNombreCarpeta());
        holder.iconoFichero.setImageResource(R.drawable.ic_file);
        if(mlista.getLinkCarpeta().equals("-")){
            holder.iconoFichero.setImageDrawable(null);  //cuando el siquiente sea nulo que se mantengha igual, porque el arraylist llega al final y tiene items vacios o algo asi
        }
        if(mlista.getLinkCarpeta().equals("linkfalso")){
            holder.iconoFichero.setImageResource(R.drawable.ic_arrow_down);
        }


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descargarFicheros(mlista.getLinkCarpeta());

            }
        });

    }

    @Override
    public int getItemCount() {
        return listaCarpetas.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout linearLayout;
        TextView folderName;
        ImageView iconoFichero;
        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            folderName=itemView.findViewById(R.id.nombreCarpeta);
            linearLayout=itemView.findViewById(R.id.LineaRecycler);
            iconoFichero= itemView.findViewById(R.id.imagenFichero);

        }
    }


    public void descargarFicheros(String link){
        final String linkdescarga=link;
        StorageReference linkDownload= mstorageReference.child(link);
        linkDownload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String nombreFile=pararNombreFichero(linkdescarga);
                String url=uri.toString();
                downloadOnSuccess(context, nombreFile,".pdf", DIRECTORY_DOWNLOADS, url );
                Toast.makeText(context, "Espere a que se termine la descarga", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void downloadOnSuccess(Context context, String fileName, String fileExtension, String destinationDir, String url){


        DownloadManager downloadManager= (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri= Uri.parse(url);
        DownloadManager.Request request= new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDir, fileName+fileExtension);
        downloadManager.enqueue(request);
    }

    public String  pararNombreFichero(String direccion){
        String nombre=direccion;
        nombre= nombre.replace("Ficheros clase","");
        nombre= nombre.replace(".pdf","");
        return nombre;
    }

}
