package api.everyonesacriticapp.ratingsservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
public class DeleteRatingTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RatingRepository mongoMock;

	@InjectMocks
	RatingController controller;

	@Test
	public void deleteRating() throws Exception {
		this.mockMvc.perform(delete("/products/633dcc5180a00f92bf00826c/ratings/rating-id/"))
			.andExpect(status().isNoContent());

	}
}