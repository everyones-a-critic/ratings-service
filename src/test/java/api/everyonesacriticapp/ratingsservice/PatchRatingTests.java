package api.everyonesacriticapp.ratingsservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
public class PatchRatingTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RatingRepository mongoMock;

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
	public void testNoContent() throws Exception {
		Rating rating = createRating(0);
		Mockito.when(mongoMock.findById("test-rating-0")).thenReturn(Optional.of(rating));
		
		this.mockMvc.perform(patch("/products/633dcc5180a00f92bf00826c/ratings/test-rating-0/")
			.header("Authorization", "eyJraWQiOiJYYVduT0g2Y0RQbkVTZktpRUdMU29wK3E5V0d3UCs5R3kwTXZMK2owdFwvQT0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJiZGJlZjY0ZS05MGRhLTRjOWYtYjQ5Ny0zYTI2ZThjZTEwNzMiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLnVzLXdlc3QtMS5hbWF6b25hd3MuY29tXC91cy13ZXN0LTFfUjZGSlRRdTFLIiwiY29nbml0bzp1c2VybmFtZSI6ImJkYmVmNjRlLTkwZGEtNGM5Zi1iNDk3LTNhMjZlOGNlMTA3MyIsIm9yaWdpbl9qdGkiOiIxYjg3YWI5ZC04NzUwLTQxNjgtOTBhYS04NDQ0ZDY2NTU0ZjIiLCJhdWQiOiIycGVwNDdyM3FxdmwwNXViODNqdGszcXVrcCIsImV2ZW50X2lkIjoiNGMxYzRkZGEtYTUzYy00ZWI2LTkxODgtMWRlMzIxYTM4NzhmIiwidG9rZW5fdXNlIjoiaWQiLCJhdXRoX3RpbWUiOjE2NjM3MDE5NzYsImV4cCI6MTY2NjAzNTQ2MSwiaWF0IjoxNjY2MDMxODYxLCJqdGkiOiIxOGNiMmEwYi02ZjAyLTQzMzktODYwYi1kYTlmYjEwMWZlMDciLCJlbWFpbCI6IjI0LmRhbmllbC5sb25nQGdtYWlsLmNvbSJ9.dDPx9eHWdT62dFZ-lWOfQ9dl9ib0AA35qGzY3f9Xobw_31GJ8VdOzyJ1tRhFJEeZTWKEgHJsIHWEal9j_JTXndj0mikxk_S_3sZhrp9rSzAaiywx9gnFiObSkqWCwSuuRE4sRhRwTWuP67eTiSTK9Phx4-Z_2114nNGdfEnHdjdMIZ2OnjIYt-rh5ypl4aeRUgW60JnWroUDgR2o-e_y3a4glmKD-AEQ83xpESsNZSxaoWTsaAnUGr2PWHGxa4IaE-p3cXABOZ5e_IbpX60AoQ9ITcqwaP1epLBDClg1KgyUFpbITJltSYgwTYIr74bEq7UruMyG-_tlIHQRf436vA")
		)
			.andExpect(status().isBadRequest());

	}

	@Test
	public void testUpdateArchived() throws Exception {
		Rating rating = createRating(0);;
		Mockito.when(mongoMock.findById("test-rating-0")).thenReturn(Optional.of(rating));
		Mockito.when(mongoMock.save(ArgumentMatchers.isA(Rating.class))).thenReturn(null);
		
		this.mockMvc.perform(patch("/products/633dcc5180a00f92bf00826c/ratings/test-rating-0/")
			.header("Authorization", "eyJraWQiOiJYYVduT0g2Y0RQbkVTZktpRUdMU29wK3E5V0d3UCs5R3kwTXZMK2owdFwvQT0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJiZGJlZjY0ZS05MGRhLTRjOWYtYjQ5Ny0zYTI2ZThjZTEwNzMiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLnVzLXdlc3QtMS5hbWF6b25hd3MuY29tXC91cy13ZXN0LTFfUjZGSlRRdTFLIiwiY29nbml0bzp1c2VybmFtZSI6ImJkYmVmNjRlLTkwZGEtNGM5Zi1iNDk3LTNhMjZlOGNlMTA3MyIsIm9yaWdpbl9qdGkiOiIxYjg3YWI5ZC04NzUwLTQxNjgtOTBhYS04NDQ0ZDY2NTU0ZjIiLCJhdWQiOiIycGVwNDdyM3FxdmwwNXViODNqdGszcXVrcCIsImV2ZW50X2lkIjoiNGMxYzRkZGEtYTUzYy00ZWI2LTkxODgtMWRlMzIxYTM4NzhmIiwidG9rZW5fdXNlIjoiaWQiLCJhdXRoX3RpbWUiOjE2NjM3MDE5NzYsImV4cCI6MTY2NjAzNTQ2MSwiaWF0IjoxNjY2MDMxODYxLCJqdGkiOiIxOGNiMmEwYi02ZjAyLTQzMzktODYwYi1kYTlmYjEwMWZlMDciLCJlbWFpbCI6IjI0LmRhbmllbC5sb25nQGdtYWlsLmNvbSJ9.dDPx9eHWdT62dFZ-lWOfQ9dl9ib0AA35qGzY3f9Xobw_31GJ8VdOzyJ1tRhFJEeZTWKEgHJsIHWEal9j_JTXndj0mikxk_S_3sZhrp9rSzAaiywx9gnFiObSkqWCwSuuRE4sRhRwTWuP67eTiSTK9Phx4-Z_2114nNGdfEnHdjdMIZ2OnjIYt-rh5ypl4aeRUgW60JnWroUDgR2o-e_y3a4glmKD-AEQ83xpESsNZSxaoWTsaAnUGr2PWHGxa4IaE-p3cXABOZ5e_IbpX60AoQ9ITcqwaP1epLBDClg1KgyUFpbITJltSYgwTYIr74bEq7UruMyG-_tlIHQRf436vA")
			.content("{\"archived\": true }")
			.contentType(MediaType.APPLICATION_JSON)
		).andDo(print())
			.andExpect(status().isOk());

			ArgumentCaptor<Rating> captor = ArgumentCaptor.forClass(Rating.class);
			verify(mongoMock).save(captor.capture());
			assertTrue(captor.getValue().archived);

	}
}