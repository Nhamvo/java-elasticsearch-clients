package com.example.testdocker.service.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.testdocker.domain.entity.Order;
import com.example.testdocker.domain.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderElsService {
    @Autowired
    private ElasticsearchClient client; // ElasticsearchClient để giao tiếp với Elasticsearch


    public List<Order> getAllOrder() throws IOException {
        // Tạo yêu cầu tìm kiếm không có điều kiện (lấy tất cả tài liệu)
        SearchRequest searchRequest = new SearchRequest.Builder()
                .index("order")  // Chỉ định chỉ mục
                .size(10000)  // Kích thước kết quả trả về (nếu cần thiết, có thể điều chỉnh)
                .build();
        // Thực thi tìm kiếm
        SearchResponse<Order> searchResponse = client.search(searchRequest, Order.class);
        // Lọc các kết quả tìm kiếm và trả về danh sách các đối tượng Product
        return searchResponse.hits().hits().stream()
                .map(Hit::source)  // Lấy source (dữ liệu) từ mỗi hit
                .collect(Collectors.toList());
    }

    // Get Product by ID
    public Order getOrderById(String id) throws IOException {
        GetResponse<Order> response = client.get(g -> g
                .index("order")
                .id(id), Order.class);
        return response.found() ? response.source() : null;
    }

    // Thêm order vào Elasticsearch
    @Transactional
    public void indexOrder(Order order) throws IOException {
        IndexRequest<Order> indexRequest = IndexRequest.of(i -> i
                .index("order") // Tên index trong Elasticsearch
                .id(order.getId().toString()) // ID của sản phẩm
                .document(order) // Dữ liệu cần index
                .refresh(Refresh.True) // Làm mới để thấy kết quả ngay
        );
        client.index(indexRequest);
    }

    @Transactional
    public void updateOrder(Order order) throws IOException {
        UpdateRequest<Order, Order> request = UpdateRequest.of(u -> u
                .index("order") // Chỉ định tên index
                .id(order.getId() + "") // Chỉ định ID của document
                .doc(order) // Dữ liệu cập nhật
        );
        client.update(request, Order.class);
    }


    // Xóa order khỏi Elasticsearch
    @Transactional
    public void deleteOrder(Order order) throws IOException {
        DeleteRequest deleteRequest = DeleteRequest.of(d -> d
                .index("order") // Tên index trong Elasticsearch
                .id(order.getId().toString()) // ID của sản phẩm
        );
        client.delete(deleteRequest);
    }
}
