function digit_speedup
   single_time=[1256.949866,1206.693085,1000.819217,841.3432649,667.157858,537.6268946,483.6894124,461.1217546];
   cluster_6_time=[842.4362825,428.283682,338.304769,245.59877,191.558715,182.1559605,160.2290615,154.2948185];
   cluster_8_time=[855.669584,464.7248445,289.00575,218.325218,177.6373845,161.7783735,135.3360375,128.206348];
   for i=1:8
       speedup_6(i)=single_time(i)/cluster_6_time(i);
       speedup_8(i)=single_time(i)/cluster_8_time(i);
   end
   N=2:2:16;
   plot(N,speedup_6,'-o',N,speedup_8,'--*');
   xlabel('N');
   ylabel('speedup');
   title('Speedup of D-ELM on Hadoop Clusters for Classification');
   axis([2 16 1 4.5]);
   legend('Cluster with 6 Nodes','Cluster with 8 Nodes');
end