package com.example.toyproject_spring.memo.controller;

import com.example.toyproject_spring.global.Rq;
import com.example.toyproject_spring.member.entity.Member;
import com.example.toyproject_spring.member.service.MemberService;
import com.example.toyproject_spring.memo.entity.Memo;
import com.example.toyproject_spring.memo.service.MemoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/memo")
public class MemoController {
    private final MemoService memoService;
    private final MemberService memberService;
    private final Rq rq;

    public MemoController(MemoService memoService, MemberService memberService, Rq rq) {
        this.memoService = memoService;
        this.memberService = memberService;
        this.rq = rq;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Memo>> list() {
        Member member = rq.getAuthenticatedActor();
        List<Memo> memoList = memoService.listByUser(member);
        return ResponseEntity.ok(memoList);
    }

    @PostMapping("/write")
    public ResponseEntity<Memo> write(@RequestBody Memo memo) {
        Member member = rq.getAuthenticatedActor();
        memo.setMember(member);
        memoService.write(memo);
        return ResponseEntity.ok(memo);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Member member = rq.getAuthenticatedActor();
        Memo memo = memoService.findByUserAndId(member, id);
        if (memo == null) {
            return ResponseEntity.notFound().build();
        }
        memoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modify(@PathVariable Long id, @RequestBody Memo memo) {
        Member member = rq.getAuthenticatedActor();
        Memo existingMemo = memoService.findByUserAndId(member, id);
        if (existingMemo == null) {
            return ResponseEntity.notFound().build();
        }
        existingMemo.setTitle(memo.getTitle());
        existingMemo.setContent(memo.getContent());
        Memo updatedMemo = memoService.modify(existingMemo.getId(), existingMemo);
        return ResponseEntity.ok(updatedMemo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable Long id) {
        Member member = rq.getAuthenticatedActor();
        Memo memo = memoService.findByUserAndId(member, id);
        if (memo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(memo);
    }
}
