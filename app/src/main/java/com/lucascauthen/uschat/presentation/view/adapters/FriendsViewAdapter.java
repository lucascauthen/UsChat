package com.lucascauthen.uschat.presentation.view.adapters;

/**
 * Created by lhc on 6/10/15.
 */
public class FriendsViewAdapter /* extends  RecyclerView.Adapter<FriendsViewAdapter.FriendViewHolder> */{
    /*
    List<Friend> friends;

    public FriendsViewAdapter(List<Friend> list) {
        this.friends = list;
    }
    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendcard_layout, parent, false);
        FriendViewHolder pvh = new FriendViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, final int position) {
        holder.friendName.setText(friends.get(position).getName());
        holder.friendshipStatus.setText(friends.get(position).getFriendshipStatusString());
        //TODO: Change to actually getting an image for the person
        holder.friendPhoto.setImageResource(R.drawable.ic_action_person_outline);
        ((ImageButton)holder.friendSettingButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) v.getContext();
                ((FriendsFragment)mainActivity.getSupportFragmentManager().findFragmentById(mainActivity.getFriendFragmentId()))
                        .onPersonSettingsRequest(position); //This is not a typo, both person and friend use this call
            }
        });
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView friendName;
        TextView friendshipStatus;
        ImageView friendPhoto;
        ImageButton friendSettingButton;

        public FriendViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.friend_cv);
            friendName = (TextView)itemView.findViewById(R.id.friend_name);
            friendshipStatus = (TextView)itemView.findViewById(R.id.friend_friendship_status);
            friendPhoto = (ImageView)itemView.findViewById(R.id.friend_photo);
            friendSettingButton = (ImageButton)itemView.findViewById(R.id.friend_settings_button);
        }
    }
    */
}
