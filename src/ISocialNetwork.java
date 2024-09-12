import java.util.Set;

public interface ISocialNetwork {

	/*
	 * join the social network and get an Account handle for logging in
	 */
	public Account join(String userName);

	/*
	 * login using a valid Account handle -- only one user can be logged in
	 * returns an updated handle to then member's account
     * no logout is necessary to switch accounts
	 */
	public Account login(Account me);


	// These operations requires a user to be logged in...

	// List all members visible to the logged-in user.
	public Set<String> listMembers();

	/*
	 * Returns true if a member has joined the social network (if visible to
	 * logged-in user)
	 */
	public boolean hasMember(String userName);

	// Send a friend request to a valid, visible member
	public void sendFriendshipTo(String userName);

	/*
	 * Block a member from befriending the logged-in user: blocked members can't
	 * see the logged-in user
	 */
	public void block(String userName);

	// Unblock a previously blocked member
	public void unblock(String userName);

	// Unfriend and existing friend
	public void sendFriendshipCancellationTo(String userName);

	// Accept a friend request from another visible member
	public void acceptFriendshipFrom(String userName);
	
	// Accept all friend requests
	public void acceptAllFriendships();

	// Reject a friend request from another member
	public void rejectFriendshipFrom(String userName);

	// Reject all friend requests
	public void rejectAllFriendships();

	/*
	 * Accept all friend requests automatically in the future, unless they are blocked by
	 * logged-in user. Once auto-acceptance is enabled, logged-in member does
	 * not need to call acceptFriendRequestFrom
	 */
	public void autoAcceptFriendships();

	// Cancel auto-acceptance, and require explicit acceptance in the future
	public void cancelAutoAcceptFriendships();

	/*
	 * Recommend friends to logged-in user: if two friends have a common friend,
	 * include that member in return Collection. Don't recommend members blocked
	 * by the logged-in user
	 */
	public Set<String> recommendFriends();

	// Leave the social network and cease to exist to other members
	public void leave();

}
