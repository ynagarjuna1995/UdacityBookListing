package io.github.ynagarjuna1995.udacitybooklisting;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.admin.booklisting.R;

import java.util.ArrayList;

class ViewHolder {
    TextView title;
    TextView author;
    ImageView myImageView;
}

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Activity context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.book_title);
            viewHolder.author = (TextView) convertView.findViewById(R.id.book_author);
            viewHolder.myImageView = (ImageView) convertView.findViewById(R.id.book_cover);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        Book currentBook = getItem(position);

        if (currentBook != null) {
            viewHolder.title.setText(currentBook.getTitle());
            viewHolder.author.setText(currentBook.getAuthor());
            Glide
                    .with(getContext())
                    .load(currentBook.getThumbnail())
                    .centerCrop()
                    .placeholder(android.R.drawable.alert_light_frame)
                    .crossFade()
                    .into(viewHolder.myImageView);
        }

        return convertView;
    }
}