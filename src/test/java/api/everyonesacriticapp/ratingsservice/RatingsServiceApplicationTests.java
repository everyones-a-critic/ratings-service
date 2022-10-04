package api.everyonesacriticapp.ratingsservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.equalTo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
public class RatingsServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductRepository mongoMock;

	@InjectMocks
	ProductController controller;

	@Test
	public void shouldReturnDefaultMessage() throws Exception {
		Product sampleProduct = new Product(
			"prod-12345",
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
			LocalDateTime.parse("2022-09-28T19:39:43"),
			"user-12345",
			LocalDateTime.parse("2022-09-28T19:39:43")
		);

		List<Product> sampleProducts = List.of(
			sampleProduct
		);
		
    	Mockito.when(mongoMock.findAll()).thenReturn(sampleProducts);

		this.mockMvc.perform(get("/products")).andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(equalTo(
				"{\"next\":null,\"previous\":null,\"results\":[{\"id\":\"prod-12345\",\"name\":\"Sample Product\",\"community_id\":\"comm-12345\",\"brand\":\"Sample Brand\",\"image_url\":\"https://www.example.com/image\",\"tasting_notes\":[\"Some\",\"Tasting\",\"Notes\"],\"price\":19.5,\"price_per\":\"12 oz\",\"categories\":[\"Some\",\"Categories\"],\"location\":\"North America\",\"process\":\"A Process\",\"variety\":\"A Variety\"}]}"
			)));
	}
}