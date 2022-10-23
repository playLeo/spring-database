package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class MemberRepositoryV0Test {

    MemberRepositoryV0 memberRepository = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
        Member member = new Member("member6", 10000);
        memberRepository.save(member);

        Member findMember = memberRepository.findById("member6");
        Assertions.assertThat(findMember).isEqualTo(member);

        memberRepository.update("member6", 20000);
        Member updateMember = memberRepository.findById("member6");
        Assertions.assertThat(updateMember.getMoney()).isEqualTo(20000);

        memberRepository.delete("member6");
        Assertions.assertThatThrownBy(() -> memberRepository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);
    }


}