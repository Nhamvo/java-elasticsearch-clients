package com.example.testdocker.service.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.example.testdocker.domain.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductElsService {


    @Autowired
    private ElasticsearchClient client; // ElasticsearchClient để giao tiếp với Elasticsearch

    public List<Product> getAllProducts() throws IOException {
        // Tạo yêu cầu tìm kiếm không có điều kiện (lấy tất cả tài liệu)
        SearchRequest searchRequest = new SearchRequest.Builder()
                .index("product")  // Chỉ định chỉ mục
                .size(10000)  // Kích thước kết quả trả về (nếu cần thiết, có thể điều chỉnh)
                .build();
        // Thực thi tìm kiếm
        SearchResponse<Product> searchResponse = client.search(searchRequest, Product.class);
        // Lọc các kết quả tìm kiếm và trả về danh sách các đối tượng Product
        return searchResponse.hits().hits().stream()
                .map(Hit::source)  // Lấy source (dữ liệu) từ mỗi hit
                .collect(Collectors.toList());
    }

    // Get Product by ID
    public Product getProductById(String id) throws IOException {
        GetResponse<Product> response = client.get(g -> g
                .index("product")
                .id(id), Product.class);

        return response.found() ? response.source() : null;
    }

    // Thêm hoặc cập nhật sản phẩm vào Elasticsearch
    public void indexProduct(Product product) throws IOException {
        IndexRequest<Product> indexRequest = IndexRequest.of(i -> i
                .index("product") // Tên index trong Elasticsearch
                .id(product.getId().toString()) // ID của sản phẩm
                .document(product) // Dữ liệu cần index
                .refresh(Refresh.True) // Làm mới để thấy kết quả ngay
        );
        client.index(indexRequest);
    }

    public void updateProduct(Product product) throws IOException {
        UpdateRequest<Product, Product> request = UpdateRequest.of(u -> u
                .index("product") // Chỉ định tên index
                .id(product.getId() + "") // Chỉ định ID của document
                .doc(product) // Dữ liệu cập nhật
        );
        client.update(request, Product.class);
    }


    // Xóa sản phẩm khỏi Elasticsearch
    public void deleteProduct(Product product) throws IOException {
        DeleteRequest deleteRequest = DeleteRequest.of(d -> d
                .index("product") // Tên index trong Elasticsearch
                .id(product.getId().toString()) // ID của sản phẩm
        );
        client.delete(deleteRequest);
    }


    // Search sản phẩm kết hợp các loại search trong 1 query
    public List<Product> searchProducts(String query) throws IOException {
        SearchRequest request = SearchRequest.of(s -> s
                        .index("product")
                        .query(q -> q
                                        .bool(b -> b
//                                .should(sh -> sh
//                                        .match(m -> m
//                                                .field("name")
//                                                .query(query) // Tìm kiếm phân tích từ
////                                                .boost(2.0f) // Tăng trọng số cho tìm kiếm chính xác
//
//                                        )
//                                )
                                                        .should(sh -> sh
                                                                .matchPhrasePrefix(mp -> mp
                                                                        .field("name")
                                                                        .query(query) // Tìm kiếm bắt đầu với cụm từ
                                                                )
                                                        )
//                                .should(sh -> sh
//                                        .wildcard(w -> w
//                                                .field("name.keyword")
//                                                .value("*" + query + "*") // Tìm kiếm chứa cụm từ
//                                        )
//                                )
//                                                        .should(sh -> sh
//                                                                .fuzzy(f -> f
//                                                                        .field("name")
//                                                                        .value(query)
//                                                                        .fuzziness("AUTO") // Tìm kiếm gần đúng
//                                                                )
//                                                        )
                                        )
                        )
        );


        SearchResponse<Product> response = client.search(request, Product.class);

        return response.hits().hits().stream()
                .map(Hit::source)  // Lấy source (dữ liệu) từ mỗi hit
                .collect(Collectors.toList());
    }


    public List<Product> multiInstanceSearchProducts(String name, Integer quantity, String manufacturer, Double minPrice, Double maxPrice) throws IOException {
        // Tạo Bool Query
        SearchRequest request = SearchRequest.of(s -> s
                .index("product")
                .query(q -> q
                        .bool(b -> {

                            // Tìm kiếm theo name (nếu có)
                            if (name != null && !name.isEmpty()) {
                                b.must(m -> m
                                        .match(mt -> mt
                                                .field("name")
                                                .query(name)
                                        )
                                );
                            }

                            // Tìm kiếm theo quantity
                            if (quantity != null) {
                                b.must(m -> m
                                        .term(r -> r
                                                .field("quantity")
                                                .value(quantity)  // GTE = lớn hơn hoặc bằng
                                        )
                                );
                            }

                            // Tìm kiếm theo manufacturer
                            if (manufacturer != null && !manufacturer.isEmpty()) {
                                b.must(m -> m
                                        .term(t -> t
                                                .field("manufacturer.keyword")  // Sử dụng keyword
                                                .value(manufacturer)
                                        )
                                );
                            }

                            // Tìm kiếm theo khoảng giá
                            if (minPrice != null || maxPrice != null) {
                                b.must(m -> m
                                        .range(r -> r
                                                .field("price")
                                                .gte(JsonData.of(minPrice != null ? minPrice : Double.NEGATIVE_INFINITY))
                                                .lte(JsonData.of(maxPrice != null ? maxPrice : Double.POSITIVE_INFINITY))
                                        )
                                );
                            }
                            return b;
                        })
                )
        );

        // Thực hiện tìm kiếm
        SearchResponse<Product> response = client.search(request, Product.class);

        // Trả về danh sách sản phẩm
        return response.hits().hits().stream()
                .map(Hit::source)  // Lấy source từ mỗi hit
                .collect(Collectors.toList());
    }
}
