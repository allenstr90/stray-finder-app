package aem.java.strayfinder.service;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.StringJoiner;

public class PaginationUtil {
    private static final String HEADER_X_TOTAL_COUNT = "X-Total-Count";
    private static final String HEADER_X_TOTAL_PAGES = "X-Total-Pages";
    private static final String HEADER_X_PAGE = "X-Page";

    private PaginationUtil() {
    }

    public static <T> HttpHeaders generatePaginationHttpHeaders(UriComponentsBuilder uriBuilder, Page<T> page) {
        int pageNumber = page.getNumber();
        int totalPages = page.getTotalPages();
        int pageSize = page.getSize();
        long totalCount = page.getTotalElements();

        // build link discovery
        StringJoiner linkBuilder = new StringJoiner(",");
        if (pageNumber < totalPages - 1) {
            linkBuilder.add(generateLink(uriBuilder, pageNumber + 1, pageSize, "next"));
        }
        if (pageNumber > 0) {
            linkBuilder.add(generateLink(uriBuilder, pageNumber - 1, pageSize, "prev"));
        }
        linkBuilder.add(generateLink(uriBuilder, totalPages - 1, pageSize, "last"))
                .add(generateLink(uriBuilder, 0, pageSize, "first"));

        // fill headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_X_TOTAL_COUNT, Long.toString(totalCount));
        headers.add(HEADER_X_TOTAL_PAGES, Integer.toString(totalPages));
        headers.add(HEADER_X_PAGE, Integer.toString(pageNumber));
        headers.add(HttpHeaders.LINK, linkBuilder.toString());

        return headers;
    }

    private static String generateLink(UriComponentsBuilder uriBuilder, int pageNumber, int pageSize, String relType) {
        String pageUri = uriBuilder.replaceQueryParam("page", Integer.toString(pageNumber))
                .replaceQueryParam("size", Integer.toString(pageSize))
                .toUriString();
        return String.format("<%s>;rel=\"%s\"", pageUri, relType);
    }

}
