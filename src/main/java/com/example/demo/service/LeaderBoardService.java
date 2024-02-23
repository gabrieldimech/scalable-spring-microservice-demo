package com.example.demo.service;

import com.example.demo.dto.LeaderBoardDTO;
import reactor.core.publisher.Mono;

public interface LeaderBoardService {
   Mono<LeaderBoardDTO> getLeaderBoard();
}
