package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "/basic/addForm";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "/basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV1(
            @RequestParam String itemName,
            @RequestParam int price,
            @RequestParam Integer quantity,
            Model model) {

        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {

        itemRepository.save(item);
   //     model.addAttribute("item", item); 자동 추가, 생략 가능

        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, Model model) {

        itemRepository.save(item);
        //     model.addAttribute("item", item); 자동 추가, 생략 가능 ModelAttribute 이름 지정 x ->
        //     클래스 첫글자만 소문자로 바꾼것이 name 으로 addAttribute 해줌

        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV4(Item item) {

        itemRepository.save(item);
        //     model.addAttribute("item", item); 자동 추가, 생략 가능 ModelAttribute 이름 지정 x ->
        //     클래스 첫글자만 소문자로 바꾼것이 name 으로 addAttribute 해줌

        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV5(Item item) {
        //PRG 패턴 -> 상품 저장 요청 결과를 뷰템플릿 반환시 -> 새로고침 -> 상품 저장 요청 다시 호출
        // 이문제 해결을 위해 상품이 저장되면 상품 상세 get 요청으로 Redirect 보내 상품 상세 페이지를 Get으로 요청하도록 수정
        // 새로고침 -> 상품 상세 페이지 get 요청
        itemRepository.save(item);

        return "redirect:/basic/items/"+item.getId();
    }

    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {

        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}
