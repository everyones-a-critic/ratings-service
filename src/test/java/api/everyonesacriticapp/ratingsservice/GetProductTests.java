package api.everyonesacriticapp.ratingsservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
public class GetProductTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductRepository mongoMock;

	@MockBean
	private Page mockPage;

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

	@Test
	public void testGetProduct() throws Exception {
		Product sampleProduct = createTestProduct(12345);

    	Mockito.when(mongoMock.findById("12345")).thenReturn(Optional.of(sampleProduct));

		this.mockMvc.perform(get("/products/12345")).andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(
				"\"id\":\"prod-12345"
			)));
	}

    @Test
	public void testNotFound() throws Exception {
    	Mockito.when(mongoMock.findById("12345")).thenReturn(Optional.empty());

		this.mockMvc.perform(get("/products/12345")).andDo(print())
			.andExpect(status().is(404));
	}
}