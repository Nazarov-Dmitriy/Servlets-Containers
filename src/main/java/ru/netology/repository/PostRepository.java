package ru.netology.repository;

import ru.netology.model.Post;

import java.util.*;

// Stub
public class PostRepository {

    final Map<Long, Post> postList = new HashMap<>();
    Long count = 1L;

    public List<Post> all() {
        List<Post> arr = new ArrayList<>();
        if (!postList.isEmpty()) {
            for (Map.Entry<Long, Post> entry : postList.entrySet()) {
                arr.add(entry.getValue());
            }
        }
        return arr;
    }

    public Optional<Post> getById(long id) {
        if (postList.containsKey(id)) {
            return Optional.ofNullable(postList.get(id));
        }
        return Optional.empty();
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            if (post.getId() < count) {
                synchronized (postList) {
                    postList.put(count, post);
                }
                count++;
            } else {
                synchronized (postList) {
                    postList.put(post.getId(), post);
                }
                count = post.getId();
            }
        } else {
            synchronized (postList) {
                postList.put(post.getId(), post);
            }
        }
        return post;
    }

    public void removeById(long id) {
            postList.remove(id);
    }
}
