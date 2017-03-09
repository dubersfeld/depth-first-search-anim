package com.dub.site;

import org.springframework.web.bind.annotation.RequestMapping;

import com.dub.config.annotation.WebController;

@WebController
public class IndexController
{
    @RequestMapping({"/", "backHome"})
    public String index()
    {
        return "depthFirstSearch/depthFirstSearch";
    }
}
