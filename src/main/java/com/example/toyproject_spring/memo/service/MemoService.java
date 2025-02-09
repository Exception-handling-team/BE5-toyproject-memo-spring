package com.example.toyproject_spring.memo.service;

import com.example.toyproject_spring.member.entity.Member;
import com.example.toyproject_spring.memo.entity.Memo;
import com.example.toyproject_spring.memo.repository.MemoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemoService {

    private final MemoRepository memoRepository;


    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    public List<Memo> listByUser(Member member) {
        return memoRepository.findByMember(member);
    }

    public Memo write(Memo memo) {
            return memoRepository.save(memo);

    }

    public void delete(Long id) {
        memoRepository.deleteById(id);
    }

    public Memo modify(Long id, Memo memo) {
        Memo existingMemo = memoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Memo not found"));

        if (!existingMemo.getMember().equals(memo.getMember())) {
            throw new IllegalArgumentException("이 메모는 수정할 수 없습니다.");
        }

        existingMemo.setTitle(memo.getTitle());
        existingMemo.setContent(memo.getContent());
        return memoRepository.save(existingMemo);
    }

    public Memo findByUserAndId(Member member, Long id) {
        return memoRepository.findByMemberAndId(member, id);
    }
}
