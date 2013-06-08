function digit_sizeup
   time_8=[110.2175175,170.5078095,233.9965815,299.7164,353.32586,413.870697,460.831414,533.61067,583.935408,639.171808];
   time_6=[112.2280735,199.6352355,252.891147,319.7628285,401.2535815,448.948366,490.874193,566.9677235,624.111616,677.6477145];
   for i=1:10
       sizeup_6(i)=time_6(i)/time_6(1);
       sizeup_8(i)=time_8(i)/time_8(1);
   end
   m=1:1:10;
   plot(m,sizeup_6,'-o',m,sizeup_8,'--*');
   xlabel('m');
   ylabel('sizeup');
   title('Sizeup of D-ELM on Hadoop Cluster for Classification');
  % axis([1 10 1 15]);
   axis normal;
   legend('Cluster with 6 Nodes','Cluster with 8 Nodes');
end