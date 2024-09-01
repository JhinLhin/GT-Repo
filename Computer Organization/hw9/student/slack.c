/**
 * CS 2110 - Spring 2024 - Homework #9
 *
 * @author Jinlin Yang
 *only free dinamically 
 * slack.c: Complete the functions!
 */

/**
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!-IMPORTANT-!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * For any function that must use malloc, if malloc returns NULL, the function
 * itself should return NULL if needs to return a pointer (or return FAILURE if
 * the function returns a int).
 */

// Do not add ANY additional includes!!!
#include "slack.h"

/* You should NOT have any global variables. */

/** createChannel
 *
 * Creates a new channel. 
 * Initially: 
 *      - The LinkedList should have a size of 0.
 *      - The accounts array should be NULL.
 *      - numUsers should be 0.
 * 
 * @return A pointer to the Channel if successful, NULL if unsuccessful.
 */
Channel *createChannel(void) {
  Channel* channel = (Channel*)malloc(sizeof(Channel));
  if (channel == NULL){
    return NULL;
  }
  channel->posts.head = NULL;
  channel->posts.size = 0;
  channel->users = NULL;
  channel->numUsers = 0;
  return channel;
  // Channel* channel = (Channel *)malloc(sizeof(Channel));
  // if (channel == NULL){
  //   return NULL;
  // }
  // //LinkedList
  // channel->posts.head = NULL;
  // channel->posts.size = 0;
  // //account array
  // channel->users = NULL;
  // //number of users
  // channel->numUsers = 0;
  // return channel;
}

/** createAccount
 *
 * Creates a new account with the provided username and accountID. 
 * 
 * Make sure to add the account to the channel. Remember, if this is
 * the first account, the account array will be NULL.
 * 
 * You will have to resize the accounts array to add the new account
 * to the channel. Think about what function you can use to help you
 * with this.
 * 

 //
 * All accountID's must be unique. If the given accountID already exists
 * in the channel, return FAILURE. You may find it helpful to implement
 * findUser() first.
 * 
 * @param channel A pointer to the channel you are adding the account to
 * @param username A pointer to a string representing the name of the account
 * @param accountID An integer representing the ID of the account
 * @return FAILURE if 
 *          - any of the parameters are NULL,
 *          - the accountID is not unique, or 
 *          - if malloc fails. 
 * SUCCESS if successful.
 */
int createAccount(Channel *channel, const char *username, int accountID) {
  if (channel == NULL || username == NULL || findUser(channel, accountID) != NULL){
    return FAILURE;
  }
  // Remember when malloc a struct, you must also allocate space for 
  // any pointer like string inside of it
  // 1. if users == NULL
    if (channel->users == NULL){
    char* str = (char*)malloc((strlen(username) + 1 )* sizeof(char));
    if (str == NULL){
      return FAILURE;
    }
    strcpy(str, username); // username
    Account* act = (Account*)malloc(sizeof(Account));
    if (act == NULL){
      free(str);
      return FAILURE;
    }
    act->username = str;
    act->accountID = accountID; // account struct
    Account** tmp = (Account**)malloc(sizeof(Account*)); 
    if (tmp == NULL){
      free(str);
      free(act); // pointer itself will get popped off but the stuff pointed by it will not
      return FAILURE;
    }
    tmp[0] = act;
    channel->users = tmp;
    channel->numUsers++;
    return SUCCESS;
  }
  // 2. if account already exist
  for (int i = 0; i <= channel->numUsers - 1; i++){
    if (channel->users[i]->accountID == accountID){
      return FAILURE;
    }
  }
  // 3. resize and add it
  char* str = (char*)malloc((strlen(username)  + 1) * sizeof(char));
  if (str == NULL){
    return FAILURE;
  }
  strcpy(str, username); // username
  Account* act = (Account*)malloc(sizeof(Account));
  if (act == NULL){
    free(str);
    return FAILURE;
  }
  act->username = str;
  act->accountID = accountID;
  Account** tmp = (Account**)realloc(channel->users, (channel->numUsers + 1) * sizeof(Account*));
  if (tmp == NULL){
    free(str);
    free(act);
    return FAILURE;
  }
  tmp[channel->numUsers] = act;
  channel->users = tmp;
  channel->numUsers++;
  return SUCCESS;
  // if (channel == NULL || username == NULL || findUser(channel, accountID) != NULL){
  //   return FAILURE;
  // }
  // if (channel->users == NULL){
  //   Account** c = (Account**)malloc(sizeof(Account*));
  //   if (c == NULL){
  //     return FAILURE;
  //   }
  //   channel->users = c;
  //   Account* p = (Account*)malloc(sizeof(Account));
  //   if (p == NULL){
  //     return FAILURE;
  //   }
  //   char* s0 = (char*)malloc(strlen(username) + 1 * sizeof(char));
  //   if (s0 == NULL){
  //     return FAILURE;
  //   }
  //   strcpy(s0, username);
  //   p->username = s0;
  //   p->accountID = accountID;
  //   *(channel->users) = p;
  //   channel->numUsers++;
  //   return SUCCESS;
  // } else {
  //   int length = (channel->numUsers) + 1; 
  //   Account** p0 = (Account**)realloc(channel->users, length * sizeof(Account*));
  //   if (p0 == NULL){
  //     return FAILURE;
  //   }
  //   channel->users = p0;// this is the array pointer

  //   Account* p1 = (Account*)malloc(sizeof(Account));
  //   if (p1 == NULL){
  //     return FAILURE;
  //   }
  //   char* s1 = (char*)malloc(strlen(username)  + 1 * sizeof(char));
  //   if (s1 == NULL){
  //     free(p1);
  //     return FAILURE;
  //   }
  //   strcpy(s1, username);
  //   p1->username = s1;
  //   p1->accountID = accountID;
  //   *((channel->users) + (channel->numUsers)) = p1;
  //   channel->numUsers++;
  //   return SUCCESS;
  // }
  // return FAILURE;
}

/** createPost
 *
 * Creates a new post with the provided text, sender ID, channel ID, and post ID.
 * Remember to add the post to the back of the LinkedList of posts in the 
 * provided channel and increment the size of the LinkedList accordingly.
 * 
 * There can be a maximum of 10 reactions on a single post (MAX_REACTION_NUM).

 * By default, no reactions are on a post.
 * 
 * All postID's must be unique. If the given postID already exists
 * in the channel, return FAILURE. You may find it helpful to implement
 * findPost() first.
 * 
 * Additionally, you must ensure that the given senderID is associated with
 * an account in the channel. You may find it helpful to implement findUser()
 * first.
 *
 * @param channel A pointer to the channel where the post will be created
 * @param text A pointer to a string representing the content of the post
 * @param senderID An integer representing the ID of the account that sent the post
 * @param postID An integer representing the unique ID of the post
 * @return FAILURE if 
 *          - any of the parameters are NULL, 
 *          - the senderID is not an account in the channel,
 *          - the postID is not unique, or
 *          - if malloc fails. 
 * SUCCESS if successful.
 */
int createPost(Channel *channel, const char *text, int senderID, int postID) {
  if (channel == NULL || text == NULL || findPost(channel, postID) != NULL || findUser(channel, senderID) == NULL){
    return FAILURE;
  }
  //1. malloc text of post
  char* t = (char*)malloc((strlen(text) + 1) * sizeof(char));
  if (t == NULL){
    return FAILURE;
  }
  strcpy(t, text);
  //2. malloc post
  Post* p = (Post*)malloc(sizeof(Post));
  if (p == NULL){
    free(t);
    return FAILURE;
  }
  p->postID = postID;
  p->senderID = senderID;
  p->text = t;
  p->numReactions = 0;
  //3. malloc node
  Node* n = (Node*)malloc(sizeof(Node));
  if (n == NULL){
    free(t);
    free(p);
    return FAILURE;
  }
  n->next = NULL;
  n->data = p;
  //4. linked node to the back 
  //if head == NULL;
  if (channel->posts.head == NULL){
    channel->posts.head = n;
  } else {
    Node* curr = channel->posts.head;
    while (curr->next != NULL){
      curr = curr->next;
    }
    curr->next = n;
  }
  channel->posts.size++;
  return SUCCESS;
  // if (channel == NULL || text == NULL || findUser(channel, senderID) == NULL || findPost(channel, postID) != NULL){
  //   return FAILURE;
  // }
  // // maybe we just malloc a node, unlike array, linked list is not stored contiguously
  // //1. malloc a post then give it to a node
  // Post* post = (Post*)malloc(sizeof(Post));
  // if (post == NULL){
  //   return FAILURE;
  // }
  // //2. malloc text since it is not fixed
  // char* t = (char*)malloc((strlen(text) + 1) * sizeof(char));
  // if (t == NULL){
  //   free(post); // we just ran out of memory, so free post
  //   return FAILURE;
  // }
  // strcpy(t, text);

  // //3. assign values to post
  // post->postID = postID;
  // post->senderID = senderID;
  // post->text = t;
  // post->numReactions = 0;
  // //maybe do not touch reactions[] and numReactions

  // //4. malloc a node and take that post
  // Node* node = (Node*)malloc(sizeof(Node));
  // if (node == NULL){
  //   free(post->text);
  //   free(post);
  //   return FAILURE;
  // }
  // node->next = NULL; // I have set newNode.next = NULL already here!
  // node->data = post;
  // //5. add that node to the back of the linkedlist
  //   //case 1: linkedlist is empty
  //   if (channel->posts.head == NULL){
  //     channel->posts.head = node;
  //     ((channel->posts).size)++;
  //     return SUCCESS;
  //   } else {
  //     //case 2: linkedlist is not empty
  //     Node* curr = channel->posts.head;
  //     while (curr->next != NULL){
  //       curr = curr->next;
  //     }
  //     curr->next = node;
  //     ((channel->posts).size)++;
  //     return SUCCESS;
  //   }
  // return FAILURE;
}

/** addReaction
 *
 * Adds a reaction to the specified post. Remember to increment numReactions
 * in the provided post accordingly and add it to the reactions array.
 * 
 * Hint: Do you need to malloc?
 * 
 * You may assume that the accountID is valid for this method. However,
 * we are declaring that a user can only react once to a post. So it is upon
 * you to enforce this constraint. For example, if the user has already reacted
 * with HEART, they cannot also react with SKULL.
 * 
 * Additionally, a post can have a maximum of 10 reactions.
 *
 * @param post A pointer to the post to which the reaction will be added
 * @param accountID An integer representing the ID of the account that reacted to the post
 * @param reaction An enum value representing the type of reaction
 * @return FAILURE if 
 *          - any of the parameters are NULL,
 *          - the post cannot have more reactions, or
 *          - the user has already reacted to the post
 * SUCCESS if successful.
 */
int addReaction(Post *post, int accountID, enum ReactionType reaction) {
  if(post == NULL || post->numReactions == MAX_REACTION_NUM){
    return FAILURE;
  }
  for (int i = 0; i <= post->numReactions - 1; i++){
    if (post->reactions[i].userID == accountID){
      return FAILURE;
    }
  }
  post->reactions[post->numReactions].userID = accountID;
  post->reactions[post->numReactions].reaction = reaction;
  post->numReactions++;
  return SUCCESS;
  // if (post == NULL || post->numReactions >= MAX_REACTION_NUM){
  //   return FAILURE;
  // }
  // for (int i = 0; i <= post->numReactions - 1; i++){
  //   if (post->reactions[i].userID == accountID){
  //     return FAILURE;
  //   }
  // }
  // post->reactions[post->numReactions].userID = accountID;
  // post->reactions[post->numReactions].reaction = reaction;
  // post->numReactions++;
  // return SUCCESS;
}

/** findUser
 *
 * Finds and returns the user with the specified userID within the
 * given channel.
 *
 * @param channel A pointer to the channel where the post will be searched
 * @param accountID An integer representing the ID of the user to be found
 * @return A pointer to the found user, or NULL if the channel is NULL or
 * no user with the given ID exists.
 */
Account* findUser(Channel *channel, int accountID) {
  if (channel == NULL || channel->users == NULL || channel->numUsers == 0){
    return NULL;
  }
  for (int i = 0; i <= channel->numUsers - 1; i++){
    if (channel->users[i]->accountID == accountID){
      return channel->users[i];
    }
  }
  return NULL;
  // if (channel == NULL || channel->numUsers == 0 || channel->users == NULL){
  //   return NULL;
  // }
  // for (int i = 0; i <= channel->numUsers - 1; i++){
  //   if ((**((channel->users) + i)).accountID == accountID){
  //     return *(channel->users + i);
  //   }
  // }
  // return NULL;
}

/** findPost
 *
 * Finds and returns the post with the specified postID within the given channel.
 *
 * @param channel A pointer to the channel where the post will be searched
 * @param postID An integer representing the ID of the post to be found
 * @return A pointer to the found post, or NULL if the channel is NULL or
 * no post with the given ID exists.
 */
Post *findPost(Channel *channel, int postID) {
  if (channel == NULL || channel->posts.head == NULL || channel->posts.size == 0){
    return NULL;
  }
  for (Node* curr = channel->posts.head; curr != NULL; curr = curr->next){
    if (curr->data->postID == postID){
      return curr->data;
    }
  }
  return NULL;
  //In C, you cannot directly check if an instance of a struct is NULL. In C, NULL is typically used to represent a null pointer, not a null struct.
  // if (channel == NULL || channel->posts.head == NULL || channel->posts.size == 0){
  //   return NULL;
  // }
  // for (Node* curr = channel->posts.head; curr != NULL; curr = curr->next){
  //   if (curr->data->postID == postID){
  //     return curr->data;
  //   }
  // }
  // return NULL;
}

/** searchForPalindrome
 *
 * Searches for the first post that is a palindrome. The text of the
 * post must be a case-sensitive palindrome (including spaces and all). 
 * 
 * Ex:  "evil rats on no star live" -> good
 *      "Evil rats on no star live" -> nope (Capitalization differs)
 *      "was it a car or a cat I saw" -> bad (the spaces ruin the palindrome)
 *      "Can I be a palindrome" -> no
 *      "rise to vote, sir" -> nope again (spaces and punctuation)
 * 
 * Return a pointer to the post that contains the palindrome. Also, set
 * the out variable names index to contain the index of the post in the
 * linked list.
 *
 * @param channel An pointer to the channel to search
 * @param index A pointer to a int that should be set to the index of the post
 *              in the Linked List. If no palindromic post is found, do not touch.
 * @return A pointer to the found post, or NULL if no palindromic post was found
 *         or if any of the parameters are NULL.
 *         Also, set the index out variable appropriately.
 */
Post *searchForPalindrome(Channel *channel, int* index) {
  if (channel == NULL || channel->posts.head == NULL || channel->posts.head->data == NULL || channel->posts.head->data->text == NULL){
    return NULL;
  }
  int i = 0;
  for (Node* curr = channel->posts.head; curr != NULL; curr = curr->next, i++){
    int j, k;
    for (j = 0, k = (int)strlen(curr->data->text) - 1; j <= (int)strlen(curr->data->text) / 2 - 1; j++, k--){
      if (curr->data->text[j] != curr->data->text[k]){
        break;
      }
    }
    if (!(j < k)){
      *index = i;
      return curr->data;
    }
  }
  return NULL;
  // if (channel == NULL || channel->posts.head == NULL || channel->posts.head->data == NULL || channel->posts.head->data->text == NULL){
  //   return NULL;
  // }
  // Post* p = NULL;
  // // each node has a post
  // int index1 = 0;
  // int i, j;
  // for (Node* curr = channel->posts.head; curr != NULL; curr = curr->next, index1++){
  //   for (i = 0, j = (int)strlen(curr->data->text) - 1; i <= (int)strlen(curr->data->text) / 2 - 1; i++, j--){
  //     if (curr->data->text[i] != curr->data->text[j]){
  //       break;
  //     }
  //   }
  //   if (i >= j){
  //     p = curr->data;
  //     *index = index1;
  //     break;
  //   }
  // }
  // return p;
}

/** deleteReaction
 *
 * Deletes a reaction from the specified post. Remember to decrement numReactions
 * and remove it from the reactions array in the provided post.
 * 
 * Make sure when you are deleting from an array, you shift the remaining elements
 * towards the beginning of the array, if needed.
 *
 * @param post A pointer to the post from which the reaction will be deleted
 * @param accountID An integer representing the ID of the user whose reaction will be deleted
 * @param reaction An enum value representing the type of reaction to be deleted
 * @return FAILURE if the post is NULL or if the reaction isn't found, SUCCESS if successful.
 */
int deleteReaction(Post *post, int accountID, enum ReactionType reaction) {
  if (post == NULL){
    return FAILURE;
  }
  for (int i = 0; i <= post->numReactions - 1; i++){
    if (post->reactions[i].userID == accountID && post->reactions[i].reaction == reaction){
      for (int j = i + 1; j <= post->numReactions - 1; j++){
        post->reactions[j - 1] = post->reactions[j];
      }
      post->numReactions--;
      return SUCCESS;
    }
  }
  return FAILURE;
  // //post> traverse the reaction array to find the user ID > once find user ID check reaction type
  // if (post == NULL){
  //   return FAILURE;
  // }
  // //reactions is an array of Reactions structs
  // int index = -1;
  // for (int i = 0; i <= post->numReactions - 1; i++){
  //   if (post->reactions[i].userID == accountID && post->reactions[i].reaction == reaction){
  //     index = i;
  //     break;
  //   }
  // }
  // if (index != -1 && post->numReactions == 1){
  //   (post->numReactions)--;
  //   return SUCCESS;
  // } else if (index != -1){
  //   for (int i = index + 1; i <= post->numReactions - 1; i++){
  //     post->reactions[i - 1] = post->reactions[i];
  //   }
  //   (post->numReactions)--;
  //   return SUCCESS;
  // }

  // return FAILURE;
}

/** deletePost
 *
 * Deletes the post with the specified post ID from the given channel. Remember
 * to remove the post of the LinkedList of posts in the provided channel, free any structs/
 * data structures, and decrement numPosts accordingly.
 *
 * @param channel A pointer to the channel where the post is located
 * @param postID An integer representing the ID of the post to be deleted
 * @return FAILURE if the channel is NULL or if the post isn't found, SUCCESS if successful.
 */
int deletePost(Channel *channel, int postID) {
  if (channel == NULL || channel->posts.head == NULL || channel->posts.size == 0){
    return FAILURE;
  }
  //If found post
    //1. relink the list
    //2. free text
    //3. free post
    //4. free Node
  //Remove head
  if (channel->posts.head->data->postID == postID){
    Node* tmp = channel->posts.head->next;
    free(channel->posts.head->data->text);
    free(channel->posts.head->data);
    free(channel->posts.head);
    channel->posts.head = tmp;
    channel->posts.size--;
    return SUCCESS;
  }
  //Remove others
  for (Node* curr = channel->posts.head; curr->next != NULL; curr = curr->next){
    if (curr->next->data->postID == postID){
      Node* tmp = curr->next->next;
      free(curr->next->data->text);
      free(curr->next->data);
      free(curr->next);
      curr->next = tmp;
      channel->posts.size--;
      return SUCCESS;
    }
  }
  //return FAILURE by default
  return FAILURE;












  // if (channel == NULL || channel->posts.head == NULL || channel->posts.head->data == NULL){
  //   return FAILURE; 
  // }

  // // We free both struct inside of each Node and the node itself, including an array of structs

  // //case 1 when head is the node we are removing
  // if (channel->posts.head->data->postID == postID){
  //   Node* temp = channel->posts.head;
  //   channel->posts.head = temp->next;
  //   free(temp->data->text);
  //   free(temp->data);
  //   free(temp);
  //   (channel->posts).size--;
  //   return SUCCESS;
  // }
  // //case 2 when node is not head
  // Node* prev = NULL;
  // for (Node* curr = channel->posts.head; curr != NULL; curr = curr->next){
  //   if (curr->data->postID == postID){
  //     Node* temp = curr;
  //     prev->next = curr->next;
  //     free(temp->data->text);
  //     free(temp->data);
  //     free(temp);
  //     (channel->posts).size--;
  //     return SUCCESS;
  //   }
  //   prev = curr;
  // }
  // return FAILURE;
}

/** deleteAccount
 *
 * Deletes the account with the specified accountID. Make sure to remove it from the accounts
 * array in the channel and decrement numAccounts accordingly. 
 * 
 * Make sure when you are deleting from an array, you shift the remaining elements, 
 * if needed.
 * 
 * When an account is deleted, all of that user's posts and reactions should be 
 * deleted as well.
 *
 * @param channel A pointer to the channel you are deleting the account from
 * @param accountID An integer representing the ID of the account to be deleted
 * @return FAILURE if the channel is NULL or if the account isn't found, SUCCESS if successful.
 */
int deleteAccount(Channel *channel, int accountID) {
  if (channel == NULL || channel->users == NULL || channel->numUsers == 0){
    return FAILURE;
  }
  for (int i = 0; i <= channel->numUsers - 1; i++){
    // if accountID is found
    if (channel->users[i]->accountID == accountID){
      //1. traverse every node to either remove post directly or remove reaction
      Node* curr = channel->posts.head;
      while (curr != NULL){
        Node* next = curr->next; // remember this line is fkin important!
        if (curr->data->senderID == accountID){
          deletePost(channel, curr->data->postID);
        } else {
          for (int j = 0; j <= curr->data->numReactions - 1; j++){
            if (curr->data->reactions[j].userID == accountID){
              deleteReaction(curr->data, accountID, curr->data->reactions[j].reaction);
              break;
            }
          }
        }
        curr = next;
      }
      //2. free the username
      free(channel->users[i]->username);
      //3. free the account
      free(channel->users[i]);
      //4. left shift
      for (int k = i + 1; k <= channel->numUsers - 1; k++){
        channel->users[k - 1] = channel->users[k];
      }
      //5. reallocate the array
      channel->users = (Account**)realloc(channel->users, (channel->numUsers - 1) * sizeof(Account*));
      channel->numUsers--;
      //channel->users = tmp;
      return SUCCESS;
    }
  }
  // Return failure by default
  return FAILURE;
  // we use 2 for loops to free reaction and post, when free, only free dynamically allocated elements
  // we do not have to free fixed size elements like int, float, which is why no need to free reaction array!
  // We find the index of that account first
  // We free username, account
  // We shift the pointer array 
  // We realloc because it will only copy over the first n elements and truncate the others
  // if (channel == NULL || channel->users == NULL || channel->numUsers == 0){
  //   return FAILURE;
  // }
  // int x; // index of the account 
  // for (x = 0; x <= channel->numUsers - 1; x++){
  //   if ((channel->users)[x]->accountID == accountID){
  //     break;
  //   }
  // }

  // if (x == channel->numUsers){ // did not find the accountID
  //   return FAILURE;
  // }
  // Node* curr = channel->posts.head;

  // while (curr != NULL){ // traverse post list and reaction array inside of it
  //   Node* temp = curr->next;
  //   if (curr->data != NULL && curr->data->senderID == accountID){
  //     // int result = deletePost(channel, curr->data->postID);
  //     // if (result == 1){
  //     //   return FAILURE;
  //     // }
  //     deletePost(channel, curr->data->postID);
  //   } else {
  //     for (int i = 0; i <= curr->data->numReactions - 1; i++){
  //       if (curr->data->reactions[i].userID == accountID){
  //         // int result = deleteReaction(curr->data, accountID, curr->data->reactions[i].reaction);
  //         // if (result == 1){
  //         //   return FAILURE;
  //         // }
  //         deleteReaction(curr->data, accountID, curr->data->reactions[i].reaction);
  //         break;
  //       }
  //     }
  //   }
  //   curr = temp;
  // }
  // free(channel->users[x]->username);
  // free(channel->users[x]); // need the account struct address to free it
  // for (int j = x + 1; j <= channel->numUsers - 1; j++){
  //   (channel->users)[j - 1] = (channel->users)[j];
  // }
  // channel->numUsers--;
  // Account** temp = (Account**)realloc(channel->users, (channel->numUsers) * sizeof(Account*));
  // // if (temp == NULL){
  // //   return FAILURE;
  // // }
  // //Don't check null if you are shrinking down, only if you are growing up
  // //If you have one node left, after the realloc temp would be NULL but we have removed successfully 
  // channel->users = temp;
  // return SUCCESS;
}

/** deleteChannel
 *
 * Deletes the channel that is passed in. Make sure to free any structs/
 * data structures contained in the channel.
 * 
 * Do nothing if the channel is NULL.
 * 
 * @param channel A pointer to the channel that you are deleting
 */
void deleteChannel(Channel *channel) {
  if (channel != NULL){
  //1. traverse through the list and delete
    Node* curr = channel->posts.head;
    while (curr != NULL){
      Node* tmp = curr->next;
      deletePost(channel, curr->data->postID);
      curr = tmp;
    }
    //2. traverse through the array and delete
    int back;
    for (back = channel->numUsers - 1; back >= 0; back--){
      deleteAccount(channel, channel->users[back]->accountID);
    }
  }
  free(channel);







  // if (channel != NULL){
  //   //1. we free the linkedlist of nodes
  //   // first free the post of each node, i think .next is a fixed amount not dynamically allocated
  //   // then we free the entire node
  //   // then we free the entire linkedlist? No because it is not dynamically allocated as well
  //   Node* curr = channel->posts.head;
  //   while (curr != NULL) {
  //     Node* tmp = curr->next; // Store the next node pointer before deleting the current node
  //     deletePost(channel, curr->data->postID);
  //     //free(curr); // Free the current node
  //     curr = tmp; // Move to the next node
  //   }
  //   //free(&(channel->posts));
  //   //2. we free the array of account pointers
  //   // we need to free each element first, then the backing array
  //   // for (int i = 0; i <= channel->numUsers - 1; i++){
  //   //   deleteAccount(channel, channel->users[0]->accountID);
  //   // }
  //   for (int i = channel->numUsers - 1; i >= 0; i--) {
  //     deleteAccount(channel, channel->users[i]->accountID); // Always delete the last account
  //   }
  //   //free(channel->users); // Free the users array, No need, the last iteration of the for loop already handles that 
  //   free(channel); // Free the channel structure itself
  // }
}
