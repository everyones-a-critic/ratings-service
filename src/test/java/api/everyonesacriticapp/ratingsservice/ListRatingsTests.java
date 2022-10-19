package api.everyonesacriticapp.ratingsservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
public class ListRatingsTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RatingRepository mongoMock;

	@MockBean
	private Page<Rating> mockPage;


	@InjectMocks
	RatingController controller;

	public static Rating createRating(Integer number) {
		return new Rating(
			"test-rating-" + String.valueOf(number), "633dcc5180a00f92bf00826c", "sample-user", 3.33, null, false,
			"sample-user", Instant.now().minus( 25 , ChronoUnit.HOURS ),
			"sample-user", Instant.now().minus( 25 , ChronoUnit.HOURS )
		);
	}

	@Test
	public void testListRatings() throws Exception {
    	List<Rating> ratings = new ArrayList<Rating>();
		ratings.add(createRating(0));
		Rating mostRecent = createRating(1);
		ratings.add(mostRecent);
		
		Mockito.when(mockPage.getContent()).thenReturn(ratings);
		Mockito.when(mongoMock.findAllByUserIdAndProductId(ArgumentMatchers.isA(String.class), ArgumentMatchers.isA(String.class), ArgumentMatchers.isA(Pageable.class))).thenReturn(mockPage);
		this.mockMvc.perform(get("/products/633dcc5180a00f92bf00826c/ratings/")
			.header("Authorization", "eyJraWQiOiJYYVduT0g2Y0RQbkVTZktpRUdMU29wK3E5V0d3UCs5R3kwTXZMK2owdFwvQT0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJiZGJlZjY0ZS05MGRhLTRjOWYtYjQ5Ny0zYTI2ZThjZTEwNzMiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLnVzLXdlc3QtMS5hbWF6b25hd3MuY29tXC91cy13ZXN0LTFfUjZGSlRRdTFLIiwiY29nbml0bzp1c2VybmFtZSI6ImJkYmVmNjRlLTkwZGEtNGM5Zi1iNDk3LTNhMjZlOGNlMTA3MyIsIm9yaWdpbl9qdGkiOiIxYjg3YWI5ZC04NzUwLTQxNjgtOTBhYS04NDQ0ZDY2NTU0ZjIiLCJhdWQiOiIycGVwNDdyM3FxdmwwNXViODNqdGszcXVrcCIsImV2ZW50X2lkIjoiNGMxYzRkZGEtYTUzYy00ZWI2LTkxODgtMWRlMzIxYTM4NzhmIiwidG9rZW5fdXNlIjoiaWQiLCJhdXRoX3RpbWUiOjE2NjM3MDE5NzYsImV4cCI6MTY2NjAzNTQ2MSwiaWF0IjoxNjY2MDMxODYxLCJqdGkiOiIxOGNiMmEwYi02ZjAyLTQzMzktODYwYi1kYTlmYjEwMWZlMDciLCJlbWFpbCI6IjI0LmRhbmllbC5sb25nQGdtYWlsLmNvbSJ9.dDPx9eHWdT62dFZ-lWOfQ9dl9ib0AA35qGzY3f9Xobw_31GJ8VdOzyJ1tRhFJEeZTWKEgHJsIHWEal9j_JTXndj0mikxk_S_3sZhrp9rSzAaiywx9gnFiObSkqWCwSuuRE4sRhRwTWuP67eTiSTK9Phx4-Z_2114nNGdfEnHdjdMIZ2OnjIYt-rh5ypl4aeRUgW60JnWroUDgR2o-e_y3a4glmKD-AEQ83xpESsNZSxaoWTsaAnUGr2PWHGxa4IaE-p3cXABOZ5e_IbpX60AoQ9ITcqwaP1epLBDClg1KgyUFpbITJltSYgwTYIr74bEq7UruMyG-_tlIHQRf436vA")
		).andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(
				"test-rating-0"
			)))
			.andExpect(content().string(containsString(
				"test-rating-1"
			)));

	}

	@Test
	public void testMostRecentFilter() throws Exception {
		List<Rating> ratings = new ArrayList<Rating>();
		ratings.add(createRating(0));
		Rating mostRecent = createRating(1);
		ratings.add(mostRecent);

		Mockito.when(mongoMock.findMostRecentByUserIdAndProductId("633dcc5180a00f92bf00826c", "bdbef64e-90da-4c9f-b497-3a26e8ce1073")).thenReturn(Optional.of(mostRecent));
		this.mockMvc.perform(get("/products/633dcc5180a00f92bf00826c/ratings/?mostRecent=true")
			.header("Authorization", "eyJraWQiOiJYYVduT0g2Y0RQbkVTZktpRUdMU29wK3E5V0d3UCs5R3kwTXZMK2owdFwvQT0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJiZGJlZjY0ZS05MGRhLTRjOWYtYjQ5Ny0zYTI2ZThjZTEwNzMiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLnVzLXdlc3QtMS5hbWF6b25hd3MuY29tXC91cy13ZXN0LTFfUjZGSlRRdTFLIiwiY29nbml0bzp1c2VybmFtZSI6ImJkYmVmNjRlLTkwZGEtNGM5Zi1iNDk3LTNhMjZlOGNlMTA3MyIsIm9yaWdpbl9qdGkiOiIxYjg3YWI5ZC04NzUwLTQxNjgtOTBhYS04NDQ0ZDY2NTU0ZjIiLCJhdWQiOiIycGVwNDdyM3FxdmwwNXViODNqdGszcXVrcCIsImV2ZW50X2lkIjoiNGMxYzRkZGEtYTUzYy00ZWI2LTkxODgtMWRlMzIxYTM4NzhmIiwidG9rZW5fdXNlIjoiaWQiLCJhdXRoX3RpbWUiOjE2NjM3MDE5NzYsImV4cCI6MTY2NjAzNTQ2MSwiaWF0IjoxNjY2MDMxODYxLCJqdGkiOiIxOGNiMmEwYi02ZjAyLTQzMzktODYwYi1kYTlmYjEwMWZlMDciLCJlbWFpbCI6IjI0LmRhbmllbC5sb25nQGdtYWlsLmNvbSJ9.dDPx9eHWdT62dFZ-lWOfQ9dl9ib0AA35qGzY3f9Xobw_31GJ8VdOzyJ1tRhFJEeZTWKEgHJsIHWEal9j_JTXndj0mikxk_S_3sZhrp9rSzAaiywx9gnFiObSkqWCwSuuRE4sRhRwTWuP67eTiSTK9Phx4-Z_2114nNGdfEnHdjdMIZ2OnjIYt-rh5ypl4aeRUgW60JnWroUDgR2o-e_y3a4glmKD-AEQ83xpESsNZSxaoWTsaAnUGr2PWHGxa4IaE-p3cXABOZ5e_IbpX60AoQ9ITcqwaP1epLBDClg1KgyUFpbITJltSYgwTYIr74bEq7UruMyG-_tlIHQRf436vA")
		).andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(
				"test-rating-1"
			)))
			.andExpect(content().string(not(containsString(
				"test-rating-0"
			))));
	}
}