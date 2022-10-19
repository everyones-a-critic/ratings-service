package api.everyonesacriticapp.ratingsservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.bson.types.ObjectId;
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
public class PostRatingTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RatingRepository mongoMock;

	@InjectMocks
	RatingController controller;

	@Test
	public void testCreateRating() throws Exception {
    	Mockito.when(mongoMock.save(ArgumentMatchers.isA(Rating.class))).thenReturn(null);
		Mockito.when(mongoMock.save(ArgumentMatchers.isA(Rating.class))).thenReturn(null);
		this.mockMvc.perform(post("/products/12345/ratings/")
		.content("{\"rating\": 4.33, \"comments\": \"This is a test\"}")
		.header("Authorization", "eyJraWQiOiJYYVduT0g2Y0RQbkVTZktpRUdMU29wK3E5V0d3UCs5R3kwTXZMK2owdFwvQT0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJiZGJlZjY0ZS05MGRhLTRjOWYtYjQ5Ny0zYTI2ZThjZTEwNzMiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLnVzLXdlc3QtMS5hbWF6b25hd3MuY29tXC91cy13ZXN0LTFfUjZGSlRRdTFLIiwiY29nbml0bzp1c2VybmFtZSI6ImJkYmVmNjRlLTkwZGEtNGM5Zi1iNDk3LTNhMjZlOGNlMTA3MyIsIm9yaWdpbl9qdGkiOiIxYjg3YWI5ZC04NzUwLTQxNjgtOTBhYS04NDQ0ZDY2NTU0ZjIiLCJhdWQiOiIycGVwNDdyM3FxdmwwNXViODNqdGszcXVrcCIsImV2ZW50X2lkIjoiNGMxYzRkZGEtYTUzYy00ZWI2LTkxODgtMWRlMzIxYTM4NzhmIiwidG9rZW5fdXNlIjoiaWQiLCJhdXRoX3RpbWUiOjE2NjM3MDE5NzYsImV4cCI6MTY2NjAzNTQ2MSwiaWF0IjoxNjY2MDMxODYxLCJqdGkiOiIxOGNiMmEwYi02ZjAyLTQzMzktODYwYi1kYTlmYjEwMWZlMDciLCJlbWFpbCI6IjI0LmRhbmllbC5sb25nQGdtYWlsLmNvbSJ9.dDPx9eHWdT62dFZ-lWOfQ9dl9ib0AA35qGzY3f9Xobw_31GJ8VdOzyJ1tRhFJEeZTWKEgHJsIHWEal9j_JTXndj0mikxk_S_3sZhrp9rSzAaiywx9gnFiObSkqWCwSuuRE4sRhRwTWuP67eTiSTK9Phx4-Z_2114nNGdfEnHdjdMIZ2OnjIYt-rh5ypl4aeRUgW60JnWroUDgR2o-e_y3a4glmKD-AEQ83xpESsNZSxaoWTsaAnUGr2PWHGxa4IaE-p3cXABOZ5e_IbpX60AoQ9ITcqwaP1epLBDClg1KgyUFpbITJltSYgwTYIr74bEq7UruMyG-_tlIHQRf436vA")
		.contentType(MediaType.APPLICATION_JSON)
		).andDo(print())
			.andExpect(status().isCreated());

		ArgumentCaptor<Rating> captor = ArgumentCaptor.forClass(Rating.class);
		verify(mongoMock).save(captor.capture());
		assertEquals(captor.getValue().rating, 4.33);
		assertEquals(captor.getValue().comments, "This is a test");
	}

	@Test
	public void testExistingRating() throws Exception {
    	Rating existingRating = new Rating(
			"test-rating", new ObjectId("633dcc5180a00f92bf00826c"), "sample-user", 3.33, null, false,
			"sample-user", Instant.now().minus( 1 , ChronoUnit.HOURS ),
			"sample-user", Instant.now().minus( 1 , ChronoUnit.HOURS )
		);
		Mockito.when(mongoMock.findMostRecentByUserIdAndProductId(new ObjectId("633dcc5180a00f92bf00826c"), "bdbef64e-90da-4c9f-b497-3a26e8ce1073")).thenReturn(Optional.of(existingRating));
		
		Mockito.when(mongoMock.save(ArgumentMatchers.isA(Rating.class))).thenReturn(null);
		this.mockMvc.perform(post("/products/633dcc5180a00f92bf00826c/ratings/")
		.content("{\"rating\": 4.33, \"comments\": \"This is a test\"}")
		.header("Authorization", "eyJraWQiOiJYYVduT0g2Y0RQbkVTZktpRUdMU29wK3E5V0d3UCs5R3kwTXZMK2owdFwvQT0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJiZGJlZjY0ZS05MGRhLTRjOWYtYjQ5Ny0zYTI2ZThjZTEwNzMiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLnVzLXdlc3QtMS5hbWF6b25hd3MuY29tXC91cy13ZXN0LTFfUjZGSlRRdTFLIiwiY29nbml0bzp1c2VybmFtZSI6ImJkYmVmNjRlLTkwZGEtNGM5Zi1iNDk3LTNhMjZlOGNlMTA3MyIsIm9yaWdpbl9qdGkiOiIxYjg3YWI5ZC04NzUwLTQxNjgtOTBhYS04NDQ0ZDY2NTU0ZjIiLCJhdWQiOiIycGVwNDdyM3FxdmwwNXViODNqdGszcXVrcCIsImV2ZW50X2lkIjoiNGMxYzRkZGEtYTUzYy00ZWI2LTkxODgtMWRlMzIxYTM4NzhmIiwidG9rZW5fdXNlIjoiaWQiLCJhdXRoX3RpbWUiOjE2NjM3MDE5NzYsImV4cCI6MTY2NjAzNTQ2MSwiaWF0IjoxNjY2MDMxODYxLCJqdGkiOiIxOGNiMmEwYi02ZjAyLTQzMzktODYwYi1kYTlmYjEwMWZlMDciLCJlbWFpbCI6IjI0LmRhbmllbC5sb25nQGdtYWlsLmNvbSJ9.dDPx9eHWdT62dFZ-lWOfQ9dl9ib0AA35qGzY3f9Xobw_31GJ8VdOzyJ1tRhFJEeZTWKEgHJsIHWEal9j_JTXndj0mikxk_S_3sZhrp9rSzAaiywx9gnFiObSkqWCwSuuRE4sRhRwTWuP67eTiSTK9Phx4-Z_2114nNGdfEnHdjdMIZ2OnjIYt-rh5ypl4aeRUgW60JnWroUDgR2o-e_y3a4glmKD-AEQ83xpESsNZSxaoWTsaAnUGr2PWHGxa4IaE-p3cXABOZ5e_IbpX60AoQ9ITcqwaP1epLBDClg1KgyUFpbITJltSYgwTYIr74bEq7UruMyG-_tlIHQRf436vA")
		.contentType(MediaType.APPLICATION_JSON)
		).andDo(print())
			.andExpect(status().isOk());

		ArgumentCaptor<Rating> captor = ArgumentCaptor.forClass(Rating.class);
		verify(mongoMock).save(captor.capture());
		assertEquals(captor.getValue().rating, 4.33);
		assertEquals(captor.getValue().comments, "This is a test");
	}

	@Test
	public void testExistingRatingTooOld() throws Exception {
    	Rating existingRating = new Rating(
			"test-rating", new ObjectId("633dcc5180a00f92bf00826c"), "sample-user", 3.33, null, false,
			"sample-user", Instant.now().minus( 25 , ChronoUnit.HOURS ),
			"sample-user", Instant.now().minus( 25 , ChronoUnit.HOURS )
		);
		Mockito.when(mongoMock.findMostRecentByUserIdAndProductId(new ObjectId("633dcc5180a00f92bf00826c"), "bdbef64e-90da-4c9f-b497-3a26e8ce1073")).thenReturn(Optional.of(existingRating));
		
		Mockito.when(mongoMock.save(ArgumentMatchers.isA(Rating.class))).thenReturn(null);
		this.mockMvc.perform(post("/products/633dcc5180a00f92bf00826c/ratings/")
		.content("{\"rating\": 4.33, \"comments\": \"This is a test\"}")
		.header("Authorization", "eyJraWQiOiJYYVduT0g2Y0RQbkVTZktpRUdMU29wK3E5V0d3UCs5R3kwTXZMK2owdFwvQT0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJiZGJlZjY0ZS05MGRhLTRjOWYtYjQ5Ny0zYTI2ZThjZTEwNzMiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLnVzLXdlc3QtMS5hbWF6b25hd3MuY29tXC91cy13ZXN0LTFfUjZGSlRRdTFLIiwiY29nbml0bzp1c2VybmFtZSI6ImJkYmVmNjRlLTkwZGEtNGM5Zi1iNDk3LTNhMjZlOGNlMTA3MyIsIm9yaWdpbl9qdGkiOiIxYjg3YWI5ZC04NzUwLTQxNjgtOTBhYS04NDQ0ZDY2NTU0ZjIiLCJhdWQiOiIycGVwNDdyM3FxdmwwNXViODNqdGszcXVrcCIsImV2ZW50X2lkIjoiNGMxYzRkZGEtYTUzYy00ZWI2LTkxODgtMWRlMzIxYTM4NzhmIiwidG9rZW5fdXNlIjoiaWQiLCJhdXRoX3RpbWUiOjE2NjM3MDE5NzYsImV4cCI6MTY2NjAzNTQ2MSwiaWF0IjoxNjY2MDMxODYxLCJqdGkiOiIxOGNiMmEwYi02ZjAyLTQzMzktODYwYi1kYTlmYjEwMWZlMDciLCJlbWFpbCI6IjI0LmRhbmllbC5sb25nQGdtYWlsLmNvbSJ9.dDPx9eHWdT62dFZ-lWOfQ9dl9ib0AA35qGzY3f9Xobw_31GJ8VdOzyJ1tRhFJEeZTWKEgHJsIHWEal9j_JTXndj0mikxk_S_3sZhrp9rSzAaiywx9gnFiObSkqWCwSuuRE4sRhRwTWuP67eTiSTK9Phx4-Z_2114nNGdfEnHdjdMIZ2OnjIYt-rh5ypl4aeRUgW60JnWroUDgR2o-e_y3a4glmKD-AEQ83xpESsNZSxaoWTsaAnUGr2PWHGxa4IaE-p3cXABOZ5e_IbpX60AoQ9ITcqwaP1epLBDClg1KgyUFpbITJltSYgwTYIr74bEq7UruMyG-_tlIHQRf436vA")
		.contentType(MediaType.APPLICATION_JSON)
		).andDo(print())
			.andExpect(status().isCreated());

		ArgumentCaptor<Rating> captor = ArgumentCaptor.forClass(Rating.class);
		verify(mongoMock).save(captor.capture());
		assertEquals(captor.getValue().rating, 4.33);
		assertEquals(captor.getValue().comments, "This is a test");
	}

	@Test
	public void testUnauthorized() throws Exception {
    	Mockito.when(mongoMock.save(ArgumentMatchers.isA(Rating.class))).thenReturn(null);
		this.mockMvc.perform(post("/products/12345/ratings/")
		.content("{\"rating\": 5.33, \"comments\": \"This is a test\"}")
		.contentType(MediaType.APPLICATION_JSON)
		).andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@Test
	public void testRatingIsRequired() throws Exception {
    	Mockito.when(mongoMock.save(ArgumentMatchers.isA(Rating.class))).thenReturn(null);
		this.mockMvc.perform(post("/products/12345/ratings/")
		.content("")
		.contentType(MediaType.APPLICATION_JSON)
		).andDo(print())
			.andExpect(status().isBadRequest());
	}
}