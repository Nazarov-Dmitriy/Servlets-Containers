package ru.netology.repository;

import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {
    final Map<Long, Post> postList = new ConcurrentHashMap<>();
    AtomicLong count = new AtomicLong(1);

    public List<Post> all() {
        List<Post> arr = new ArrayList<>();
        if (!postList.isEmpty()) {
            for (Map.Entry<Long, Post> entry : postList.entrySet()) {
                arr.add(entry.getValue());
            }
        }
        return arr;
    }

    public Optional<Post> getById(Long id) {
        return Optional.ofNullable(postList.get(id));

    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            if (post.getId() < count.get()) {
                postList.put(count.get(), post);
                count.getAndIncrement();
            } else {
                synchronized (postList) {
                    postList.put(post.getId(), post);
                }
                count.addAndGet(post.getId());
            }
        } else {
            synchronized (postList) {
                postList.put(post.getId(), post);
            }
        }
        return post;
    }

    public void removeById(Long id) {
        postList.remove(id);
    }
}
