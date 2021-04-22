package com.meetfriend.app.ui.adapters;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.meetfriend.app.R;
import com.meetfriend.app.databinding.ItemLeftChatBinding;
import com.meetfriend.app.databinding.ItemRightChatBinding;
import com.meetfriend.app.responseclasses.ChatPOJO;
import com.meetfriend.app.ui.activities.ChatActivity;
import com.meetfriend.app.ui.activities.FullScreenActivity;
import com.meetfriend.app.ui.activities.VideoPlayActivity;

import java.io.IOException;
import java.util.List;

import contractorssmart.app.utilsclasses.CommonMethods;
import contractorssmart.app.utilsclasses.PreferenceHandler;

import static com.meetfriend.app.ui.activities.ChatActivity.mediaPlayer;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final String CHAT_AUDIO = "2";
    public final String CHAT_VIDEO = "3";
    public final String CHAT_IMAGE = "4";
    public final String CHAT_GIFT = "5";
    public String CHAT_TEXT = "1";
    Handler handler = new Handler();
    private Context mContext;
    private List<ChatPOJO.Message> list;
    private String loggedInUser, otherUserImage, userImage;
    private int oldPosition = -1;

    public ChatAdapter(Context mContext, List<ChatPOJO.Message> list, String userImage, String otherUserImage) {
        this.mContext = mContext;
        this.list = list;
        this.otherUserImage = otherUserImage;
        this.userImage = userImage;
        loggedInUser = PreferenceHandler.INSTANCE.readString(mContext, "USER_ID", "");
        hasStableIds();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return viewType == 0 ? new LeftHolder(ItemLeftChatBinding.inflate(LayoutInflater.from(mContext), parent, false)) :
                new RightHolder(ItemRightChatBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
        setHasStableIds(true);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (!list.get(position).fromID.equals(loggedInUser)) {
            LeftHolder h = (LeftHolder) holder;
            h.bind();
        } else {
            RightHolder h = (RightHolder) holder;
            h.bind();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).fromID.equals(loggedInUser) ? 1 : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void initMediaPlayer(String from, String outputFile, ImageView mPlay, AppCompatSeekBar mSeekBarRight) {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        }
        handler = new Handler();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mPlay.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_play_circle_filled_24));
            }
        });

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            // Set the data source to the mediaFile location
            mediaPlayer.setDataSource(outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnPreparedListener(mp -> {
            mPlay.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_pause_circle_filled_24));
            mediaPlayer.start();
            mSeekBarRight.setEnabled(true);
            mSeekBarRight.setMax(mediaPlayer.getDuration() / 1000);
        });
        Runnable mUpdateTimeTask = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    mSeekBarRight.setProgress(mCurrentPosition);
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(mUpdateTimeTask, 1000);


        mSeekBarRight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying() && fromUser) {
                        mediaPlayer.seekTo(progress * 1000);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    class LeftHolder extends RecyclerView.ViewHolder {

        ItemLeftChatBinding binding;

        LeftHolder(ItemLeftChatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind() {
            binding.swipeRevelRight.close(true);
            ChatPOJO.Message item = list.get(getAdapterPosition());
            binding.setData(item);
            CommonMethods.INSTANCE.setUserImage(binding.mUserImageLeft, otherUserImage);
            if (item.msgTYPE.equalsIgnoreCase(CHAT_GIFT)) {
                binding.mGiftLayout.setVisibility(View.VISIBLE);
                binding.mMessageLeft.setVisibility(View.GONE);
                CommonMethods.INSTANCE.setImage(binding.mImage, item.file);
                binding.mCoin.setText(item.msgTEXT);
                binding.mImageLeft.setVisibility(View.GONE);
                binding.mAudioLayoutLeft.setVisibility(View.GONE);
                binding.mImageLayout.setVisibility(View.GONE);
            } else if (item.msgTYPE.equalsIgnoreCase(CHAT_AUDIO)) {
                binding.mGiftLayout.setVisibility(View.GONE);
                binding.mPlayVideo.setVisibility(View.GONE);
                binding.mMessageLeft.setVisibility(View.GONE);
                binding.mImageLayout.setVisibility(View.GONE);
                binding.mAudioLayoutLeft.setVisibility(View.VISIBLE);
                binding.mImageLeft.setVisibility(View.GONE);
                binding.mAudioTimeLeft.setText(item.msgTEXT);
                binding.mSeekBarLeft.setEnabled(false);
                binding.mPlayLeft.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_play_circle_filled_24));
                binding.mPlayLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (oldPosition != -1)
                            notifyItemChanged(oldPosition);
                        oldPosition = getAdapterPosition();
                        binding.mPlayLeft.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_pause_circle_filled_24));

                        initMediaPlayer("left", item.file, binding.mPlayLeft, binding.mSeekBarLeft);

                    }
                });
                binding.mAudioLayoutLeft.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ChatActivity.downloadPermission(item.file);
                        return false;
                    }
                });
            } else if (item.msgTYPE.equalsIgnoreCase(CHAT_IMAGE)) {
                binding.mGiftLayout.setVisibility(View.GONE);
                CommonMethods.INSTANCE.setImage(binding.mImageLeft, item.file);
                binding.mMessageLeft.setVisibility(View.GONE);
                binding.mImageLeft.setVisibility(View.VISIBLE);
                binding.mImageLayout.setVisibility(View.VISIBLE);
                binding.mPlayVideo.setVisibility(View.GONE);
                binding.mAudioLayoutLeft.setVisibility(View.GONE);
                binding.mImageLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, FullScreenActivity.class);
                        intent.putExtra("url", item.file);
                        mContext.startActivity(intent);
                    }
                });
                binding.mImageLeft.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ChatActivity.downloadPermission(item.file);
                        return false;
                    }
                });
            } else if (item.msgTYPE.equalsIgnoreCase(CHAT_VIDEO)) {
                binding.mGiftLayout.setVisibility(View.GONE);
                binding.mMessageLeft.setVisibility(View.GONE);
                binding.mImageLeft.setVisibility(View.VISIBLE);
                binding.mImageLayout.setVisibility(View.VISIBLE);
                binding.mPlayVideo.setVisibility(View.VISIBLE);
                binding.mAudioLayoutLeft.setVisibility(View.GONE);
                Glide.with(mContext).load(item.file).placeholder(R.drawable.image_placeholder).into(binding.mImageLeft);
                binding.mImageLeft.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ChatActivity.downloadPermission(item.file);
                        return false;
                    }
                });
                binding.mImageLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, VideoPlayActivity.class);
                        intent.putExtra("path", item.file);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                binding.mGiftLayout.setVisibility(View.GONE);
                binding.mMessageLeft.setVisibility(View.VISIBLE);
                binding.mImageLeft.setVisibility(View.GONE);
                binding.mAudioLayoutLeft.setVisibility(View.GONE);
                binding.mImageLayout.setVisibility(View.GONE);

            }
            binding.mMessageLeft.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(binding.mMessageLeft.getText());
                    Toast.makeText(mContext, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

        }
    }

    class RightHolder extends RecyclerView.ViewHolder {

        ItemRightChatBinding binding;

        RightHolder(ItemRightChatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind() {
            binding.swipeRevelRight.close(true);

            ChatPOJO.Message item = list.get(getAdapterPosition());
            binding.setData(item);
            CommonMethods.INSTANCE.setUserImage(binding.mUserImageRight, userImage);
            if (item.msgTYPE.equalsIgnoreCase(CHAT_GIFT)) {
                binding.mGiftLayout.setVisibility(View.VISIBLE);
                binding.mMessageRight.setVisibility(View.GONE);
                CommonMethods.INSTANCE.setImage(binding.mImage, item.file);
                binding.mCoin.setText(item.msgTEXT);
                binding.mAudioLayoutRight.setVisibility(View.GONE);
                binding.mImageRight.setVisibility(View.GONE);
                binding.mImageLayout.setVisibility(View.GONE);
            } else if (item.msgTYPE.equalsIgnoreCase(CHAT_AUDIO)) {
                binding.mMessageRight.setVisibility(View.GONE);
                binding.mAudioLayoutRight.setVisibility(View.VISIBLE);
                binding.mGiftLayout.setVisibility(View.GONE);
                binding.mImageRight.setVisibility(View.GONE);
                binding.mAudioTimeRight.setText(item.msgTEXT);
                binding.mImageLayout.setVisibility(View.GONE);
                binding.mPlayRight.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_play_circle_filled_24));
                binding.mSeekBarRight.setEnabled(false);
                binding.mPlayRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (oldPosition != -1)
                            notifyItemChanged(oldPosition);
                        oldPosition = getAdapterPosition();
                        binding.mPlayRight.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_pause_circle_filled_24));

                        initMediaPlayer("right", item.file, binding.mPlayRight, binding.mSeekBarRight);

                    }
                });
                binding.mAudioLayoutRight.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ChatActivity.downloadPermission(item.file);
                        return false;
                    }
                });
            } else if (item.msgTYPE.equalsIgnoreCase(CHAT_IMAGE)) {
                binding.mPlayVideo.setVisibility(View.GONE);
                binding.mMessageRight.setVisibility(View.GONE);
                binding.mImageLayout.setVisibility(View.VISIBLE);
                binding.mImageRight.setVisibility(View.VISIBLE);
                CommonMethods.INSTANCE.setImage(binding.mImageRight, item.file);
                binding.mAudioLayoutRight.setVisibility(View.GONE);
                binding.mGiftLayout.setVisibility(View.GONE);
                binding.mImageLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, FullScreenActivity.class);
                        intent.putExtra("url", item.file);
                        mContext.startActivity(intent);
                    }
                });
                binding.mImageRight.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ChatActivity.downloadPermission(item.file);
                        return false;
                    }
                });
            } else if (item.msgTYPE.equalsIgnoreCase(CHAT_VIDEO)) {
                binding.mImageRight.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ChatActivity.downloadPermission(item.file);
                        return false;
                    }
                });
                binding.mMessageRight.setVisibility(View.GONE);
                binding.mPlayVideo.setVisibility(View.VISIBLE);
                binding.mImageLayout.setVisibility(View.VISIBLE);
                binding.mImageRight.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(item.file).placeholder(R.drawable.image_placeholder).into(binding.mImageRight);
                binding.mAudioLayoutRight.setVisibility(View.GONE);
                binding.mGiftLayout.setVisibility(View.GONE);
                binding.mImageLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, VideoPlayActivity.class);
                        intent.putExtra("path", item.file);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                binding.mImageLayout.setVisibility(View.GONE);
                binding.mGiftLayout.setVisibility(View.GONE);
                binding.mMessageRight.setVisibility(View.VISIBLE);
                binding.mAudioLayoutRight.setVisibility(View.GONE);
                binding.mImageRight.setVisibility(View.GONE);

            }
            binding.mMessageRight.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(binding.mMessageRight.getText());
                    Toast.makeText(mContext, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ChatActivity.delete(getAdapterPosition());
            }
        });
        }
    }
}