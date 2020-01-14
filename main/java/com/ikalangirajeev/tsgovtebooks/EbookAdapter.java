package com.ikalangirajeev.tsgovtebooks;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class EbookAdapter extends RecyclerView.Adapter<EbookAdapter.MyViewHolder> implements Filterable {

    public interface OnItemLongClickListener {
        void onItemLongClicked(EbookItem eBookItem);
    }


    private static final String TAG = "EbookAdapter";
    private ArrayList<EbookItem> eBookList;
    private ArrayList<EbookItem> eBookListFull;
    private String stringToSpan;
    private LayoutInflater layoutInflater;
    private Context context;

    private OnItemLongClickListener onItemLongClickListener;


    EbookAdapter(Context context, ArrayList<EbookItem> ebookList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.eBookList = ebookList;
        eBookListFull = new ArrayList<>(ebookList);
    }


    void setOnItemLongClickListener(OnItemLongClickListener listener) {
        onItemLongClickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.ebook_item_cardview, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int position) {
        EbookItem currentEbookItem = eBookList.get(position);
        myViewHolder.setData(currentEbookItem);
        myViewHolder.setListeners();
    }


    @Override
    public int getItemCount() {
        return eBookList.size();
    }


    @Override
    public Filter getFilter() {
        return eBookFilter;
    }


    private Filter eBookFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<EbookItem> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(eBookListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (EbookItem item : eBookListFull) {
                    if (item.getParaName().toLowerCase().contains(filterPattern) ||
                            item.getParaDesc().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            stringToSpan = constraint.toString();
            eBookList.clear();
            eBookList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView paraNameTextView;
        TextView paraDescTextView;
        ImageView shareData;

        MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            paraNameTextView = itemView.findViewById(R.id.para_name);
            paraDescTextView = itemView.findViewById(R.id.para_desc);
            shareData = itemView.findViewById(R.id.image_send);
        }

        void setData(EbookItem currentEbookItem) {
            String spanDescName = currentEbookItem.getParaDesc();
            String spanParaName = currentEbookItem.getParaName();

            if (stringToSpan != null && !stringToSpan.isEmpty()) {
                int startPos = spanDescName.toLowerCase(Locale.US).indexOf(stringToSpan.toLowerCase(Locale.US));
                int endPos = startPos + stringToSpan.length();

                if (startPos != -1) {
                    Spannable spannableDesc = new SpannableString(spanDescName);

                    BackgroundColorSpan backgroundColorSpanYellow = new BackgroundColorSpan(Color.WHITE);
                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.CYAN);

                    spannableDesc.setSpan(foregroundColorSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableDesc.setSpan(backgroundColorSpanYellow, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    this.paraDescTextView.setText(spannableDesc);
                }
            } else {
                this.paraDescTextView.setText(spanDescName);
                this.paraDescTextView.setAlpha(0);
                this.paraDescTextView.animate().alpha(1).setDuration(1000);
            }
            this.paraNameTextView.setText(spanParaName);
            this.paraNameTextView.setTranslationX(1500);
            this.paraNameTextView.animate().translationXBy(-1500).setDuration(1500);

            this.shareData.setTranslationX(1500);
            this.shareData.animate().translationXBy(-1500).setDuration(1500);
        }

        void setListeners() {
            shareData.setOnClickListener(MyViewHolder.this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.image_send:
                    //Toast.makeText(context, "Share imageview pressed...", Toast.LENGTH_LONG).show();
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, paraNameTextView.getText().toString() + " \n\n" +
                            paraDescTextView.getText().toString());
                    sendIntent.setType("text/plain");
                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    //Verfy Intent resolve to an activity
                    if (shareIntent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(shareIntent);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
