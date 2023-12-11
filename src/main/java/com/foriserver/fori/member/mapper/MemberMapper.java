package com.foriserver.fori.member.mapper;

import com.foriserver.fori.member.dto.MemberDto;
import com.foriserver.fori.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberPostDtoToMember(MemberDto.Post memberPostDto);
    Member memberPatchDtoToMember(MemberDto.Patch memberPatchDto);

    // Login
    MemberDto.Login memberToLoginDto(Member member);

    // Check
    MemberDto.CheckPassword checkPasswordToMember(MemberDto.CheckPassword checkPasswordDto);

    // Response
    MemberDto.MemberPostResponse memberToMemberPostResponseDto(Member member);
    MemberDto.MemberPatchResponse memberToMemberPatchResponseDto(Member member);


}
