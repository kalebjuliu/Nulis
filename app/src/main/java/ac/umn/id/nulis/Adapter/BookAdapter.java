package ac.umn.id.nulis.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ac.umn.id.nulis.HelperClass.Book;
import ac.umn.id.nulis.R;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    Context context;
    ArrayList<Book> list;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onEditClick(int position);

    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public BookAdapter(Context context, ArrayList<Book> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.book_item, parent, false);
        return new BookViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = list.get(position);
        holder.bookTitle.setText(book.getTitle());
        holder.bookDesc.setText(book.getDesc());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder{
        TextView bookTitle, bookDesc;
        ImageView bookDelete, bookEdit;

        public BookViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            bookTitle = itemView.findViewById(R.id.tv_book_title);
            bookDesc = itemView.findViewById(R.id.tv_book_desc);
            bookDelete = itemView.findViewById(R.id.delete_book);
            bookEdit = itemView.findViewById(R.id.edit_book);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            bookDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

            bookEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onEditClick(position);
                        }
                    }
                }
            });
        }
    }
}
