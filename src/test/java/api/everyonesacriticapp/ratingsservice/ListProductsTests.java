package api.everyonesacriticapp.ratingsservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsString;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
public class ListProductsTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductRepository mongoMock;

	@MockBean(name="main")
	private Page<Product> mockPage;

	@MockBean(name="alt")
	private Page<Product> mockPageAlt;

	@InjectMocks
	ProductController controller;

	private static Product createTestProduct(int productNumber) {
		return new Product(
			"prod-" + String.valueOf(productNumber),
			"Sample Product",
			"comm-12345",
			"Sample Brand",
			"https://www.example.com/image",
			new ArrayList<String>(List.of("Some", "Tasting", "Notes")),
			19.5,
			"12 oz",
			new ArrayList<String>(List.of("Some", "Categories")),
			"North America",
			"A Process",
			"A Variety",
			"user-12345",
			Instant.parse("2022-09-28T19:39:43.00Z"),
			"user-12345",
			Instant.parse("2022-09-28T19:39:43.00Z")
		);
	}

	private static Product createTestProduct(int productNumber, String communityId) {
		return new Product(
			"prod-" + String.valueOf(productNumber),
			"Sample Product",
			communityId,
			"Sample Brand",
			"https://www.example.com/image",
			new ArrayList<String>(List.of("Some", "Tasting", "Notes")),
			19.5,
			"12 oz",
			new ArrayList<String>(List.of("Some", "Categories")),
			"North America",
			"A Process",
			"A Variety",
			"user-12345",
			Instant.parse("2022-09-28T19:39:43.00Z"),
			"user-12345",
			Instant.parse("2022-09-28T19:39:43.00Z")
		);
	}

	@Test
	public void testSinglePage() throws Exception {
		Product sampleProduct = createTestProduct(12345);

		List<Product> sampleProducts = List.of(
			sampleProduct
		);
		
		Mockito.when(mockPage.getContent()).thenReturn(sampleProducts);
    	Mockito.when(mongoMock.findAll(ArgumentMatchers.isA(Pageable.class))).thenReturn(mockPage);

		this.mockMvc.perform(get("/products")
			.header("Authorization", "eyJraWQiOiJYYVduT0g2Y0RQbkVTZktpRUdMU29wK3E5V0d3UCs5R3kwTXZMK2owdFwvQT0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJiZGJlZjY0ZS05MGRhLTRjOWYtYjQ5Ny0zYTI2ZThjZTEwNzMiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLnVzLXdlc3QtMS5hbWF6b25hd3MuY29tXC91cy13ZXN0LTFfUjZGSlRRdTFLIiwiY29nbml0bzp1c2VybmFtZSI6ImJkYmVmNjRlLTkwZGEtNGM5Zi1iNDk3LTNhMjZlOGNlMTA3MyIsIm9yaWdpbl9qdGkiOiIxYjg3YWI5ZC04NzUwLTQxNjgtOTBhYS04NDQ0ZDY2NTU0ZjIiLCJhdWQiOiIycGVwNDdyM3FxdmwwNXViODNqdGszcXVrcCIsImV2ZW50X2lkIjoiNGMxYzRkZGEtYTUzYy00ZWI2LTkxODgtMWRlMzIxYTM4NzhmIiwidG9rZW5fdXNlIjoiaWQiLCJhdXRoX3RpbWUiOjE2NjM3MDE5NzYsImV4cCI6MTY2NjAzNTQ2MSwiaWF0IjoxNjY2MDMxODYxLCJqdGkiOiIxOGNiMmEwYi02ZjAyLTQzMzktODYwYi1kYTlmYjEwMWZlMDciLCJlbWFpbCI6IjI0LmRhbmllbC5sb25nQGdtYWlsLmNvbSJ9.dDPx9eHWdT62dFZ-lWOfQ9dl9ib0AA35qGzY3f9Xobw_31GJ8VdOzyJ1tRhFJEeZTWKEgHJsIHWEal9j_JTXndj0mikxk_S_3sZhrp9rSzAaiywx9gnFiObSkqWCwSuuRE4sRhRwTWuP67eTiSTK9Phx4-Z_2114nNGdfEnHdjdMIZ2OnjIYt-rh5ypl4aeRUgW60JnWroUDgR2o-e_y3a4glmKD-AEQ83xpESsNZSxaoWTsaAnUGr2PWHGxa4IaE-p3cXABOZ5e_IbpX60AoQ9ITcqwaP1epLBDClg1KgyUFpbITJltSYgwTYIr74bEq7UruMyG-_tlIHQRf436vA")
		).andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(equalTo(
				"{\"next\":null,\"previous\":null,\"results\":[{\"id\":\"prod-12345\",\"name\":\"Sample Product\",\"community_id\":\"comm-12345\",\"brand\":\"Sample Brand\",\"image_url\":\"https://www.example.com/image\",\"tasting_notes\":[\"Some\",\"Tasting\",\"Notes\"],\"price\":19.5,\"price_per\":\"12 oz\",\"categories\":[\"Some\",\"Categories\"],\"location\":\"North America\",\"process\":\"A Process\",\"variety\":\"A Variety\",\"created_date\":\"2022-09-28T19:39:43Z\",\"modified_date\":\"2022-09-28T19:39:43Z\"}]}"
			)));
	}

	@Test
	public void testFirstPage() throws Exception {
		List<Product> sampleProducts = new ArrayList<Product>();
		int i = 0;
		while (i < 1) {
			sampleProducts.add(createTestProduct(i));
			i++;
		}
		
		Mockito.when(mockPage.getContent()).thenReturn(sampleProducts);
		Mockito.when(mockPage.getNumber()).thenReturn(0);
		Mockito.when(mockPage.getTotalPages()).thenReturn(3);
    	Mockito.when(mongoMock.findAll(ArgumentMatchers.isA(Pageable.class))).thenReturn(mockPage);

		this.mockMvc.perform(get("/products")
			.header("Authorization", "eyJraWQiOiJYYVduT0g2Y0RQbkVTZktpRUdMU29wK3E5V0d3UCs5R3kwTXZMK2owdFwvQT0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJiZGJlZjY0ZS05MGRhLTRjOWYtYjQ5Ny0zYTI2ZThjZTEwNzMiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLnVzLXdlc3QtMS5hbWF6b25hd3MuY29tXC91cy13ZXN0LTFfUjZGSlRRdTFLIiwiY29nbml0bzp1c2VybmFtZSI6ImJkYmVmNjRlLTkwZGEtNGM5Zi1iNDk3LTNhMjZlOGNlMTA3MyIsIm9yaWdpbl9qdGkiOiIxYjg3YWI5ZC04NzUwLTQxNjgtOTBhYS04NDQ0ZDY2NTU0ZjIiLCJhdWQiOiIycGVwNDdyM3FxdmwwNXViODNqdGszcXVrcCIsImV2ZW50X2lkIjoiNGMxYzRkZGEtYTUzYy00ZWI2LTkxODgtMWRlMzIxYTM4NzhmIiwidG9rZW5fdXNlIjoiaWQiLCJhdXRoX3RpbWUiOjE2NjM3MDE5NzYsImV4cCI6MTY2NjAzNTQ2MSwiaWF0IjoxNjY2MDMxODYxLCJqdGkiOiIxOGNiMmEwYi02ZjAyLTQzMzktODYwYi1kYTlmYjEwMWZlMDciLCJlbWFpbCI6IjI0LmRhbmllbC5sb25nQGdtYWlsLmNvbSJ9.dDPx9eHWdT62dFZ-lWOfQ9dl9ib0AA35qGzY3f9Xobw_31GJ8VdOzyJ1tRhFJEeZTWKEgHJsIHWEal9j_JTXndj0mikxk_S_3sZhrp9rSzAaiywx9gnFiObSkqWCwSuuRE4sRhRwTWuP67eTiSTK9Phx4-Z_2114nNGdfEnHdjdMIZ2OnjIYt-rh5ypl4aeRUgW60JnWroUDgR2o-e_y3a4glmKD-AEQ83xpESsNZSxaoWTsaAnUGr2PWHGxa4IaE-p3cXABOZ5e_IbpX60AoQ9ITcqwaP1epLBDClg1KgyUFpbITJltSYgwTYIr74bEq7UruMyG-_tlIHQRf436vA")
		).andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(
				"\"next\":\"/products?page=2"
			)))
			.andExpect(content().string(containsString(
				"\"previous\":null"
			)));
	}

	@Test
	public void testMiddlePage() throws Exception {
		List<Product> sampleProducts = new ArrayList<Product>();
		int i = 0;
		while (i < 1) {
			sampleProducts.add(createTestProduct(i));
			i++;
		}
		
		Mockito.when(mockPage.getContent()).thenReturn(sampleProducts);
		Mockito.when(mockPage.getNumber()).thenReturn(1);
		Mockito.when(mockPage.getTotalPages()).thenReturn(3);
    	Mockito.when(mongoMock.findAll(ArgumentMatchers.isA(Pageable.class))).thenReturn(mockPage);

		this.mockMvc.perform(get("/products")
			.header("Authorization", "eyJraWQiOiJYYVduT0g2Y0RQbkVTZktpRUdMU29wK3E5V0d3UCs5R3kwTXZMK2owdFwvQT0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJiZGJlZjY0ZS05MGRhLTRjOWYtYjQ5Ny0zYTI2ZThjZTEwNzMiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLnVzLXdlc3QtMS5hbWF6b25hd3MuY29tXC91cy13ZXN0LTFfUjZGSlRRdTFLIiwiY29nbml0bzp1c2VybmFtZSI6ImJkYmVmNjRlLTkwZGEtNGM5Zi1iNDk3LTNhMjZlOGNlMTA3MyIsIm9yaWdpbl9qdGkiOiIxYjg3YWI5ZC04NzUwLTQxNjgtOTBhYS04NDQ0ZDY2NTU0ZjIiLCJhdWQiOiIycGVwNDdyM3FxdmwwNXViODNqdGszcXVrcCIsImV2ZW50X2lkIjoiNGMxYzRkZGEtYTUzYy00ZWI2LTkxODgtMWRlMzIxYTM4NzhmIiwidG9rZW5fdXNlIjoiaWQiLCJhdXRoX3RpbWUiOjE2NjM3MDE5NzYsImV4cCI6MTY2NjAzNTQ2MSwiaWF0IjoxNjY2MDMxODYxLCJqdGkiOiIxOGNiMmEwYi02ZjAyLTQzMzktODYwYi1kYTlmYjEwMWZlMDciLCJlbWFpbCI6IjI0LmRhbmllbC5sb25nQGdtYWlsLmNvbSJ9.dDPx9eHWdT62dFZ-lWOfQ9dl9ib0AA35qGzY3f9Xobw_31GJ8VdOzyJ1tRhFJEeZTWKEgHJsIHWEal9j_JTXndj0mikxk_S_3sZhrp9rSzAaiywx9gnFiObSkqWCwSuuRE4sRhRwTWuP67eTiSTK9Phx4-Z_2114nNGdfEnHdjdMIZ2OnjIYt-rh5ypl4aeRUgW60JnWroUDgR2o-e_y3a4glmKD-AEQ83xpESsNZSxaoWTsaAnUGr2PWHGxa4IaE-p3cXABOZ5e_IbpX60AoQ9ITcqwaP1epLBDClg1KgyUFpbITJltSYgwTYIr74bEq7UruMyG-_tlIHQRf436vA")
		).andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(
				"\"next\":\"/products?page=3"
			)))
			.andExpect(content().string(containsString(
				"\"previous\":\"/products?page=1"
			)));
	}

	@Test
	public void testLastPage() throws Exception {
		List<Product> sampleProducts = new ArrayList<Product>();
		int i = 0;
		while (i < 1) {
			sampleProducts.add(createTestProduct(i));
			i++;
		}
		
		Mockito.when(mockPage.getContent()).thenReturn(sampleProducts);
		Mockito.when(mockPage.getNumber()).thenReturn(2);
		Mockito.when(mockPage.getTotalPages()).thenReturn(3);
    	Mockito.when(mongoMock.findAll(ArgumentMatchers.isA(Pageable.class))).thenReturn(mockPage);

		this.mockMvc.perform(get("/products")
			.header("Authorization", "eyJraWQiOiJYYVduT0g2Y0RQbkVTZktpRUdMU29wK3E5V0d3UCs5R3kwTXZMK2owdFwvQT0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJiZGJlZjY0ZS05MGRhLTRjOWYtYjQ5Ny0zYTI2ZThjZTEwNzMiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLnVzLXdlc3QtMS5hbWF6b25hd3MuY29tXC91cy13ZXN0LTFfUjZGSlRRdTFLIiwiY29nbml0bzp1c2VybmFtZSI6ImJkYmVmNjRlLTkwZGEtNGM5Zi1iNDk3LTNhMjZlOGNlMTA3MyIsIm9yaWdpbl9qdGkiOiIxYjg3YWI5ZC04NzUwLTQxNjgtOTBhYS04NDQ0ZDY2NTU0ZjIiLCJhdWQiOiIycGVwNDdyM3FxdmwwNXViODNqdGszcXVrcCIsImV2ZW50X2lkIjoiNGMxYzRkZGEtYTUzYy00ZWI2LTkxODgtMWRlMzIxYTM4NzhmIiwidG9rZW5fdXNlIjoiaWQiLCJhdXRoX3RpbWUiOjE2NjM3MDE5NzYsImV4cCI6MTY2NjAzNTQ2MSwiaWF0IjoxNjY2MDMxODYxLCJqdGkiOiIxOGNiMmEwYi02ZjAyLTQzMzktODYwYi1kYTlmYjEwMWZlMDciLCJlbWFpbCI6IjI0LmRhbmllbC5sb25nQGdtYWlsLmNvbSJ9.dDPx9eHWdT62dFZ-lWOfQ9dl9ib0AA35qGzY3f9Xobw_31GJ8VdOzyJ1tRhFJEeZTWKEgHJsIHWEal9j_JTXndj0mikxk_S_3sZhrp9rSzAaiywx9gnFiObSkqWCwSuuRE4sRhRwTWuP67eTiSTK9Phx4-Z_2114nNGdfEnHdjdMIZ2OnjIYt-rh5ypl4aeRUgW60JnWroUDgR2o-e_y3a4glmKD-AEQ83xpESsNZSxaoWTsaAnUGr2PWHGxa4IaE-p3cXABOZ5e_IbpX60AoQ9ITcqwaP1epLBDClg1KgyUFpbITJltSYgwTYIr74bEq7UruMyG-_tlIHQRf436vA")
		).andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(
				"\"next\":null"
			)))
			.andExpect(content().string(containsString(
				"\"previous\":\"/products?page=2"
			)));
	}

	@Test
	public void testQueryParamRetention() throws Exception {
		List<Product> sampleProducts = new ArrayList<Product>();
		int i = 0;
		while (i < 1) {
			sampleProducts.add(createTestProduct(i));
			i++;
		}
		
		Mockito.when(mockPage.getContent()).thenReturn(sampleProducts);
		Mockito.when(mockPage.getNumber()).thenReturn(0);
		Mockito.when(mockPage.getTotalPages()).thenReturn(3);
    	Mockito.when(mongoMock.findAll(ArgumentMatchers.isA(Pageable.class))).thenReturn(mockPage);

		this.mockMvc.perform(get("/products?test=hi")
			.header("Authorization", "eyJraWQiOiJYYVduT0g2Y0RQbkVTZktpRUdMU29wK3E5V0d3UCs5R3kwTXZMK2owdFwvQT0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJiZGJlZjY0ZS05MGRhLTRjOWYtYjQ5Ny0zYTI2ZThjZTEwNzMiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLnVzLXdlc3QtMS5hbWF6b25hd3MuY29tXC91cy13ZXN0LTFfUjZGSlRRdTFLIiwiY29nbml0bzp1c2VybmFtZSI6ImJkYmVmNjRlLTkwZGEtNGM5Zi1iNDk3LTNhMjZlOGNlMTA3MyIsIm9yaWdpbl9qdGkiOiIxYjg3YWI5ZC04NzUwLTQxNjgtOTBhYS04NDQ0ZDY2NTU0ZjIiLCJhdWQiOiIycGVwNDdyM3FxdmwwNXViODNqdGszcXVrcCIsImV2ZW50X2lkIjoiNGMxYzRkZGEtYTUzYy00ZWI2LTkxODgtMWRlMzIxYTM4NzhmIiwidG9rZW5fdXNlIjoiaWQiLCJhdXRoX3RpbWUiOjE2NjM3MDE5NzYsImV4cCI6MTY2NjAzNTQ2MSwiaWF0IjoxNjY2MDMxODYxLCJqdGkiOiIxOGNiMmEwYi02ZjAyLTQzMzktODYwYi1kYTlmYjEwMWZlMDciLCJlbWFpbCI6IjI0LmRhbmllbC5sb25nQGdtYWlsLmNvbSJ9.dDPx9eHWdT62dFZ-lWOfQ9dl9ib0AA35qGzY3f9Xobw_31GJ8VdOzyJ1tRhFJEeZTWKEgHJsIHWEal9j_JTXndj0mikxk_S_3sZhrp9rSzAaiywx9gnFiObSkqWCwSuuRE4sRhRwTWuP67eTiSTK9Phx4-Z_2114nNGdfEnHdjdMIZ2OnjIYt-rh5ypl4aeRUgW60JnWroUDgR2o-e_y3a4glmKD-AEQ83xpESsNZSxaoWTsaAnUGr2PWHGxa4IaE-p3cXABOZ5e_IbpX60AoQ9ITcqwaP1epLBDClg1KgyUFpbITJltSYgwTYIr74bEq7UruMyG-_tlIHQRf436vA")
		).andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(
				"\"next\":\"/products?test=hi&page=2"
			)))
			.andExpect(content().string(containsString(
				"\"previous\":null"
			)));
	}

	@Test
	public void testCommunityFilter() throws Exception {
		List<Product> sampleProducts = new ArrayList<Product>();
		sampleProducts.add(createTestProduct(1, "633c9a8ca8c3f241bf1df1b3"));

		List<Product> sampleProductsAlt = new ArrayList<Product>();
		sampleProductsAlt.add(createTestProduct(2));
		
		Mockito.when(mockPage.getContent()).thenReturn(sampleProducts);
		Mockito.when(mockPage.getNumber()).thenReturn(0);
		Mockito.when(mockPage.getTotalPages()).thenReturn(1);

		Mockito.when(mockPageAlt.getContent()).thenReturn(sampleProductsAlt);
		Mockito.when(mockPageAlt.getNumber()).thenReturn(0);
		Mockito.when(mockPageAlt.getTotalPages()).thenReturn(1);

		Mockito.when(mongoMock.findAll(ArgumentMatchers.isA(Pageable.class))).thenReturn(mockPage);
		Mockito.when(mongoMock.findAllByCommunityId(ArgumentMatchers.isA(ObjectId.class), ArgumentMatchers.isA(Pageable.class))).thenReturn(mockPageAlt);

		this.mockMvc.perform(get("/products?communityId=633c9a8ca8c3f241bf1df1b3")
			.header("Authorization", "eyJraWQiOiJYYVduT0g2Y0RQbkVTZktpRUdMU29wK3E5V0d3UCs5R3kwTXZMK2owdFwvQT0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJiZGJlZjY0ZS05MGRhLTRjOWYtYjQ5Ny0zYTI2ZThjZTEwNzMiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLnVzLXdlc3QtMS5hbWF6b25hd3MuY29tXC91cy13ZXN0LTFfUjZGSlRRdTFLIiwiY29nbml0bzp1c2VybmFtZSI6ImJkYmVmNjRlLTkwZGEtNGM5Zi1iNDk3LTNhMjZlOGNlMTA3MyIsIm9yaWdpbl9qdGkiOiIxYjg3YWI5ZC04NzUwLTQxNjgtOTBhYS04NDQ0ZDY2NTU0ZjIiLCJhdWQiOiIycGVwNDdyM3FxdmwwNXViODNqdGszcXVrcCIsImV2ZW50X2lkIjoiNGMxYzRkZGEtYTUzYy00ZWI2LTkxODgtMWRlMzIxYTM4NzhmIiwidG9rZW5fdXNlIjoiaWQiLCJhdXRoX3RpbWUiOjE2NjM3MDE5NzYsImV4cCI6MTY2NjAzNTQ2MSwiaWF0IjoxNjY2MDMxODYxLCJqdGkiOiIxOGNiMmEwYi02ZjAyLTQzMzktODYwYi1kYTlmYjEwMWZlMDciLCJlbWFpbCI6IjI0LmRhbmllbC5sb25nQGdtYWlsLmNvbSJ9.dDPx9eHWdT62dFZ-lWOfQ9dl9ib0AA35qGzY3f9Xobw_31GJ8VdOzyJ1tRhFJEeZTWKEgHJsIHWEal9j_JTXndj0mikxk_S_3sZhrp9rSzAaiywx9gnFiObSkqWCwSuuRE4sRhRwTWuP67eTiSTK9Phx4-Z_2114nNGdfEnHdjdMIZ2OnjIYt-rh5ypl4aeRUgW60JnWroUDgR2o-e_y3a4glmKD-AEQ83xpESsNZSxaoWTsaAnUGr2PWHGxa4IaE-p3cXABOZ5e_IbpX60AoQ9ITcqwaP1epLBDClg1KgyUFpbITJltSYgwTYIr74bEq7UruMyG-_tlIHQRf436vA")
		).andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(
				"\"id\":\"prod-2"
			)));
	}

	@Test
	public void testWithRatingsFilter() throws Exception {
		List<Product> sampleProducts = new ArrayList<Product>();
		sampleProducts.add(createTestProduct(1, "633c9a8ca8c3f241bf1df1b3"));

		List<Product> sampleProductsAlt = new ArrayList<Product>();
		sampleProductsAlt.add(createTestProduct(2));
		
		Mockito.when(mockPage.getContent()).thenReturn(sampleProducts);
		Mockito.when(mockPage.getNumber()).thenReturn(0);
		Mockito.when(mockPage.getTotalPages()).thenReturn(1);

		Mockito.when(mockPageAlt.getContent()).thenReturn(sampleProductsAlt);
		Mockito.when(mockPageAlt.getNumber()).thenReturn(0);
		Mockito.when(mockPageAlt.getTotalPages()).thenReturn(1);

		Mockito.when(mongoMock.findAll(ArgumentMatchers.isA(Pageable.class))).thenReturn(mockPage);
		Mockito.when(mongoMock.findAllByCommunityIdWithRatings(ArgumentMatchers.isA(ObjectId.class), ArgumentMatchers.isA(String.class), ArgumentMatchers.isA(Pageable.class))).thenReturn(mockPageAlt);

		this.mockMvc.perform(get("/products?communityId=633c9a8ca8c3f241bf1df1b3&withRatings=true")
			.header("Authorization", "eyJraWQiOiJYYVduT0g2Y0RQbkVTZktpRUdMU29wK3E5V0d3UCs5R3kwTXZMK2owdFwvQT0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJiZGJlZjY0ZS05MGRhLTRjOWYtYjQ5Ny0zYTI2ZThjZTEwNzMiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLnVzLXdlc3QtMS5hbWF6b25hd3MuY29tXC91cy13ZXN0LTFfUjZGSlRRdTFLIiwiY29nbml0bzp1c2VybmFtZSI6ImJkYmVmNjRlLTkwZGEtNGM5Zi1iNDk3LTNhMjZlOGNlMTA3MyIsIm9yaWdpbl9qdGkiOiIxYjg3YWI5ZC04NzUwLTQxNjgtOTBhYS04NDQ0ZDY2NTU0ZjIiLCJhdWQiOiIycGVwNDdyM3FxdmwwNXViODNqdGszcXVrcCIsImV2ZW50X2lkIjoiNGMxYzRkZGEtYTUzYy00ZWI2LTkxODgtMWRlMzIxYTM4NzhmIiwidG9rZW5fdXNlIjoiaWQiLCJhdXRoX3RpbWUiOjE2NjM3MDE5NzYsImV4cCI6MTY2NjAzNTQ2MSwiaWF0IjoxNjY2MDMxODYxLCJqdGkiOiIxOGNiMmEwYi02ZjAyLTQzMzktODYwYi1kYTlmYjEwMWZlMDciLCJlbWFpbCI6IjI0LmRhbmllbC5sb25nQGdtYWlsLmNvbSJ9.dDPx9eHWdT62dFZ-lWOfQ9dl9ib0AA35qGzY3f9Xobw_31GJ8VdOzyJ1tRhFJEeZTWKEgHJsIHWEal9j_JTXndj0mikxk_S_3sZhrp9rSzAaiywx9gnFiObSkqWCwSuuRE4sRhRwTWuP67eTiSTK9Phx4-Z_2114nNGdfEnHdjdMIZ2OnjIYt-rh5ypl4aeRUgW60JnWroUDgR2o-e_y3a4glmKD-AEQ83xpESsNZSxaoWTsaAnUGr2PWHGxa4IaE-p3cXABOZ5e_IbpX60AoQ9ITcqwaP1epLBDClg1KgyUFpbITJltSYgwTYIr74bEq7UruMyG-_tlIHQRf436vA")
		).andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(
				"\"id\":\"prod-2"
			)));
	}
}