package ru.netology.repository;

import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {
    final Map<AtomicLong, Post> postList = new ConcurrentHashMap<>();
    AtomicLong count = new AtomicLong(1);

    public List<Post> all() {
        List<Post> arr = new ArrayList<>();
        if (!postList.isEmpty()) {
            for (Map.Entry<AtomicLong, Post> entry : postList.entrySet()) {
                arr.add(entry.getValue());
            }
        }
        return arr;
    }

    public Optional<Post> getById(AtomicLong id) {
        if (postList.containsKey(id)) {
            return Optional.ofNullable(postList.get(id));
        }
        return Optional.empty();
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            if (post.getId() < count.get()) {
                postList.put(count, post);
                count.getAndIncrement();
            } else {
                synchronized (postList) {
                    postList.put(new AtomicLong(post.getId()), post);
                }
                count.addAndGet(post.getId())  ;
            }
        } else {
            synchronized (postList) {
                postList.put(new AtomicLong(post.getId()), post);
            }
        }
        return post;
    }

    public void removeById(AtomicLong id) {
        postList.remove(id);
    }
}
